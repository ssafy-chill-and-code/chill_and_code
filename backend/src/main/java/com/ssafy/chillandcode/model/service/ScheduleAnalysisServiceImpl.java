package com.ssafy.chillandcode.model.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.ScheduleDao;
import com.ssafy.chillandcode.model.dto.schedule.AnalysisStatus;
import com.ssafy.chillandcode.model.dto.schedule.DayAnalysisResult;
import com.ssafy.chillandcode.model.dto.schedule.Schedule;
import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleSummaryResponse;
import com.ssafy.chillandcode.recommend.period.BusyDayEvaluator;
import com.ssafy.chillandcode.recommend.period.PeriodRecommendationContext;

/**
 * 일정 분석 전용 Service
 *
 * - 추천 로직을 건드리지 않고
 * - 일정 패턴을 "설명"하기 위한 분석 결과만 생성한다
 */
@Service
public class ScheduleAnalysisServiceImpl implements ScheduleAnalysisService {

	@Autowired
	private ScheduleDao scheduleDao;

	private final BusyDayEvaluator busyDayEvaluator = new BusyDayEvaluator();

	/**
	 * 월 단위 일정 분석 메인 엔트리
	 */
	@Override
	public ScheduleSummaryResponse analyzeMonth(long userId, YearMonth month) {

		/* =========================
		 * 1. 월 범위 일정 조회
		 * ========================= */
		LocalDateTime start = month.atDay(1).atStartOfDay();
		LocalDateTime end = month.atEndOfMonth().atTime(LocalTime.MAX);

		List<Schedule> schedules =
				scheduleDao.selectScheduleByRange(userId, start, end);

		/* =========================
		 * 2. 날짜별 일정 분해
		 * (날짜 걸침 일정 포함)
		 * ========================= */
		Map<LocalDate, List<Schedule>> dailySchedules =
				splitSchedulesByDate(schedules);

		/* =========================
		 * 3. 하루 단위 분석 (Step 6)
		 * ========================= */
		List<DayAnalysisResult> dayResults = new ArrayList<>();
		PeriodRecommendationContext context =
				PeriodRecommendationContext.forAnalysis();

		for (LocalDate d = month.atDay(1);
			 !d.isAfter(month.atEndOfMonth());
			 d = d.plusDays(1)) {

			List<Schedule> daySchedules =
					dailySchedules.getOrDefault(d, List.of());

			dayResults.add(analyzeOneDay(d, daySchedules, context));
		}

		/* =========================
		 * 4. 월 단위 집계 (Step 7)
		 * ========================= */
		MonthAggregation agg = aggregateMonth(dayResults);

		/* =========================
		 * 5. overallStatus 계산 (Step 8)
		 * ========================= */
		AnalysisStatus overallStatus =
				calcOverallStatus(agg.busyDays, agg.totalDays);

		/* =========================
		 * 6. 일정 타입 비율 (Step 9)
		 * ========================= */
		Map<ScheduleType, Double> typeRatio =
				calcScheduleTypeRatio(schedules);

		/* =========================
		 * 7. 요약 문구 생성 (Step 11)
		 * ========================= */
		String summaryComment =
				buildSummaryComment(month, overallStatus, agg);

		/* =========================
		 * 8. 최종 Response 조립
		 * ========================= */
		ScheduleSummaryResponse res = new ScheduleSummaryResponse();
		res.setMonth(month);
		res.setOverallStatus(overallStatus);
		res.setTotalDays(agg.totalDays);
		res.setMixedDayCount(agg.mixedDays);
		res.setFlexibleDayCount(agg.flexibleDays);
		res.setMostBusyWeek(agg.mostBusyWeek);
		res.setRecommendWeeks(agg.recommendWeeks);
		res.setScheduleTypeRatio(typeRatio);
		res.setSummaryComment(summaryComment);

		return res;
	}

	/* =========================================================
	 * 하루 단위 분석 (Step 6)
	 * ========================================================= */

	private DayAnalysisResult analyzeOneDay(
			LocalDate date,
			List<Schedule> schedules,
			PeriodRecommendationContext context
	) {
		// 일정 없음 → FLEXIBLE
		if (schedules.isEmpty()) {
			return DayAnalysisResult.flexible(date, "NO_SCHEDULE");
		}

		// 추천 로직 BUSY 판단 (절대 기준)
		Map<LocalDate, List<Schedule.Tag>> tagMap = new HashMap<>();
		tagMap.put(date, extractTags(schedules));

		Boolean busy =
				busyDayEvaluator.calculateBusyDays(tagMap, context).get(date);

		if (Boolean.TRUE.equals(busy)) {
			return DayAnalysisResult.busy(date, "RECOMMEND_BUSY");
		}

		// 연속 일정 블록 분석
		List<TimeBlock> blocks = mergeIntervals(schedules);

		if (maxContinuousHours(blocks) >= 5) {
			return DayAnalysisResult.mixed(date, "LONG_CONTINUOUS_BLOCK");
		}

		long totalHours =
				blocks.stream().mapToLong(TimeBlock::durationHours).sum();

		if (totalHours >= 8 && schedules.size() >= 3) {
			return DayAnalysisResult.mixed(date, "HIGH_DENSITY_DAY");
		}

		return DayAnalysisResult.flexible(date, "SHORT_SCATTERED");
	}

	/* =========================================================
	 * 월 단위 집계 (Step 7)
	 * ========================================================= */

	private MonthAggregation aggregateMonth(List<DayAnalysisResult> days) {

		MonthAggregation agg = new MonthAggregation(days.size());
		WeekFields wf = WeekFields.of(Locale.KOREA);

		for (DayAnalysisResult d : days) {
			agg.count(d.getStatus());

			int week = d.getDate().get(wf.weekOfMonth());
			agg.accumulateWeek(week, d.getStatus());
		}

		agg.finish();
		return agg;
	}

	/* =========================================================
	 * overallStatus 계산 (Step 8)
	 * ========================================================= */

	private AnalysisStatus calcOverallStatus(int busyDays, int totalDays) {

		if (totalDays == 0) return AnalysisStatus.FLEXIBLE;

		double ratio = (double) busyDays / totalDays;

		if (ratio >= 0.4) return AnalysisStatus.BUSY;
		if (ratio >= 0.2) return AnalysisStatus.MIXED;
		return AnalysisStatus.FLEXIBLE;
	}

	/* =========================================================
	 * 일정 타입 비율 (Step 9)
	 * ========================================================= */

	private Map<ScheduleType, Double> calcScheduleTypeRatio(
			List<Schedule> schedules
	) {
		Map<ScheduleType, Long> count =
				schedules.stream().collect(
						Collectors.groupingBy(
								Schedule::getScheduleType,
								Collectors.counting()
						)
				);

		long total = schedules.size();
		Map<ScheduleType, Double> ratio = new EnumMap<>(ScheduleType.class);

		for (ScheduleType t : ScheduleType.values()) {
			ratio.put(t, total == 0 ? 0.0 :
					count.getOrDefault(t, 0L) / (double) total);
		}
		return ratio;
	}

	/* =========================================================
	 * 요약 문구 (Step 11)
	 * ========================================================= */

	private String buildSummaryComment(
			YearMonth month,
			AnalysisStatus status,
			MonthAggregation agg
	) {
		if (status == AnalysisStatus.BUSY) {
			return "이번 달은 일정 밀도가 높아 이동이 부담될 수 있어요.";
		}
		if (status == AnalysisStatus.MIXED) {
			return "바쁜 날과 여유로운 날이 섞여 있는 달이에요.";
		}
		if (!agg.recommendWeeks.isEmpty()) {
			return "여유로운 주가 있어 워케이션을 고려해볼 수 있어요.";
		}
		return "전반적으로 일정이 분산된 달이에요.";
	}

	/* =========================================================
	 * 내부 집계용 클래스 (외부 노출 X)
	 * ========================================================= */

	private static class MonthAggregation {
		int totalDays;
		int busyDays;
		int mixedDays;
		int flexibleDays;

		int mostBusyWeek = -1;
		List<Integer> recommendWeeks = new ArrayList<>();

		Map<Integer, WeekAgg> weekMap = new HashMap<>();

		MonthAggregation(int totalDays) {
			this.totalDays = totalDays;
		}

		void count(AnalysisStatus s) {
			if (s == AnalysisStatus.BUSY) busyDays++;
			else if (s == AnalysisStatus.MIXED) mixedDays++;
			else flexibleDays++;
		}

		void accumulateWeek(int week, AnalysisStatus s) {
			weekMap.computeIfAbsent(week, WeekAgg::new).add(s);
		}

		void finish() {
			int maxBusy = 0;

			for (WeekAgg w : weekMap.values()) {
				if (w.busy > maxBusy) {
					maxBusy = w.busy;
					mostBusyWeek = w.week;
				}
				if (w.busy == 0 && w.maxFlexibleStreak >= 3) {
					recommendWeeks.add(w.week);
				}
			}
			if (maxBusy == 0) mostBusyWeek = -1;
		}
	}

	private static class WeekAgg {
		int week;
		int busy;
		int currentFlex;
		int maxFlexibleStreak;

		WeekAgg(int week) {
			this.week = week;
		}

		void add(AnalysisStatus s) {
			if (s == AnalysisStatus.BUSY) {
				busy++;
				currentFlex = 0;
			} else if (s == AnalysisStatus.FLEXIBLE) {
				currentFlex++;
				maxFlexibleStreak = Math.max(maxFlexibleStreak, currentFlex);
			} else {
				currentFlex = 0;
			}
		}
	}

	/* =========================================================
	 * 공통 유틸
	 * ========================================================= */

	private Map<LocalDate, List<Schedule>> splitSchedulesByDate(
			List<Schedule> schedules
	) {
		Map<LocalDate, List<Schedule>> map = new HashMap<>();

		for (Schedule s : schedules) {
			LocalDate start = s.getStartDateTime().toLocalDate();
			LocalDate end = s.getEndDateTime().toLocalDate();

			for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
				map.computeIfAbsent(d, k -> new ArrayList<>()).add(s);
			}
		}
		return map;
	}

	private List<Schedule.Tag> extractTags(List<Schedule> schedules) {
		List<Schedule.Tag> tags = new ArrayList<>();
		for (Schedule s : schedules) {
			// 추천 로직과 동일: userTag가 있으면 userTag만, 없으면 autoTag만 사용
			Schedule.Tag tag = s.getUserTag() != null ? s.getUserTag() : s.getAutoTag();
			if (tag != null) {
				tags.add(tag);
			}
		}
		return tags;
	}

	private List<TimeBlock> mergeIntervals(List<Schedule> schedules) {
		List<TimeBlock> blocks = new ArrayList<>();
		for (Schedule s : schedules) {
			blocks.add(new TimeBlock(
					s.getStartDateTime(),
					s.getEndDateTime()
			));
		}

		blocks.sort(Comparator.comparing(b -> b.start));

		List<TimeBlock> merged = new ArrayList<>();
		for (TimeBlock cur : blocks) {
			if (merged.isEmpty()) {
				merged.add(cur);
				continue;
			}
			TimeBlock last = merged.get(merged.size() - 1);
			if (!cur.start.isAfter(last.end)) {
				last.end = last.end.isAfter(cur.end) ? last.end : cur.end;
			} else {
				merged.add(cur);
			}
		}
		return merged;
	}

	private long maxContinuousHours(List<TimeBlock> blocks) {
		return blocks.stream()
				.mapToLong(TimeBlock::durationHours)
				.max()
				.orElse(0);
	}

	private static class TimeBlock {
		LocalDateTime start;
		LocalDateTime end;

		TimeBlock(LocalDateTime s, LocalDateTime e) {
			start = s;
			end = e;
		}

		long durationHours() {
			return Duration.between(start, end).toHours();
		}
	}
}
