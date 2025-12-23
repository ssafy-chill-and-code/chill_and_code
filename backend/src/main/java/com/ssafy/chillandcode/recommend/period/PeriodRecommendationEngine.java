package com.ssafy.chillandcode.recommend.period;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.chillandcode.model.dto.schedule.Schedule;
import com.ssafy.chillandcode.model.dto.schedule.Schedule.Tag;

public class PeriodRecommendationEngine {

	/**
	 * 기간 추천(분석) 흐름 0. 특정 기간 일정 리스트를 넘겨받음 1. 날짜 단위 flatten + tag 수집 2. 옵션 반영해
	 * busy/free 판정 3. 연속 free 기간 추출 (min~max) 4. scoring + TOP1 선정
	 */

	PeriodCandidate longestCandidate;
	PeriodCandidate fastestCandidate;
	PeriodCandidate weekendCandidate;

	public List<PeriodCandidate> recommend(List<Schedule> schedules, PeriodRecommendationContext context) {

		longestCandidate = null;
		fastestCandidate = null;
		weekendCandidate = null;
		
		// 1. 날짜 단위 flatten + 태그 수집
		Map<LocalDate, List<Tag>> days = flattenSchedules(schedules, context);

		// 2. 옵션 반영해 busy/free 판정
		BusyDayEvaluator busyDayEvaluator = new BusyDayEvaluator();
		Map<LocalDate, Boolean> busyMap = busyDayEvaluator.calculateBusyDays(days, context);

		// 3. 연속 free 기간 추출
		findFreeSegments(busyMap, context);
		
		List<PeriodCandidate> result = new ArrayList<>();
		
		if(longestCandidate != null) {
			result.add(longestCandidate);
		}
		
		if(fastestCandidate != null) {
			result.add(fastestCandidate);
		}
		
		if(weekendCandidate != null) {
			result.add(weekendCandidate);
		}
		
		return result;
	}

	/**
	 * 1. 날짜 단위 flatten + tag 수집
	 */
	public Map<LocalDate, List<Tag>> flattenSchedules(List<Schedule> schedules, PeriodRecommendationContext context) {

		Map<LocalDate, List<Tag>> result = new HashMap<>();

		for (Schedule s : schedules) {

			// autoTag랑 userTag 중에서 뽑아올 Tag 결정
			Tag tag = resolveFinalTag(s);

			LocalDate start = s.getStartDateTime().toLocalDate();
			LocalDate end = s.getEndDateTime().toLocalDate();

			// 기간 추천 시 고려할 일정 범위를 벗어나는지 체크
			// 일정이 검색 범위에 걸쳐 있는 경우, 실제 추천 대상 날짜만 잘라서 사용한다
			LocalDate realStart = start.isBefore(context.getSearchStart()) ? context.getSearchStart() : start;
			LocalDate realEnd = end.isAfter(context.getSearchEnd()) ? context.getSearchEnd() : end;

			// 순회하면서 Map에 기간 추가하고 Tag 추가하기
			for (LocalDate d = realStart; !d.isAfter(realEnd); d = d.plusDays(1)) {
				result.computeIfAbsent(d, k -> new ArrayList<>()).add(tag);
			}
		}

		return result;
	}

	private Tag resolveFinalTag(Schedule s) {
		if (s.getUserTag() == null) {
			return s.getAutoTag();
		}

		return s.getUserTag();
	}


	/**
	 * 3. 연속 free 기간 추출 후 후보 생성 (min~max)
	 */
	public void findFreeSegments(Map<LocalDate, Boolean> busyMap, PeriodRecommendationContext context) {

		boolean inFreeSegment = false;
		LocalDate segmentStart = null;
		LocalDate segmentEnd = null;

		// realStart, realEnd 값을 기준으로 날짜에 plus 1 씩하면서 검색을 통한 순회
		for (LocalDate d = context.getSearchStart(); !d.isAfter(context.getSearchEnd()); d = d.plusDays(1)) {

			// free 날짜
			if (!busyMap.getOrDefault(d, false)) {
				if (!inFreeSegment) {
					inFreeSegment = true;
					segmentStart = d;
				}
			}
			// busy 날짜
			else {
				if (inFreeSegment) {
					inFreeSegment = false;
					segmentEnd = d.minusDays(1);

					onFreeSegmentEnd(segmentStart, segmentEnd, context);
				}
			}
		}

		if (inFreeSegment) {
			onFreeSegmentEnd(segmentStart, context.getSearchEnd(), context);
		}
	}

	// free 구간 종료 로직
	private void onFreeSegmentEnd(LocalDate segmentStart, LocalDate segmentEnd, PeriodRecommendationContext context) {
		int duration = (int) segmentStart.until(segmentEnd, ChronoUnit.DAYS) + 1;

		if (duration < context.getMinDays()) {
			return;
		}

		/**
		 * A. 가장 긴 기간: maxDays를 넘지 않는 선에서 최대 길이 
		 * B. 가장 빠른 시작 후보: segmentStart부터 시작해서 minDays를 충족하는 기간 
		 * C. 주말을 가장 많이 포함하는 연속기간
		 */

		// A 후보 갱신
		int candidateLength = Math.min(duration, context.getMaxDays());
		LocalDate candidateStart = segmentStart;
		LocalDate candidateEnd = candidateStart.plusDays(candidateLength - 1);

		if (longestCandidate == null || longestCandidate.getDurationDays() < candidateLength) {
			longestCandidate = new PeriodCandidate(candidateStart, candidateEnd, candidateLength,
					CandidateType.LONGEST);
		}

		// B 후보 갱신
		if (fastestCandidate == null || fastestCandidate.getStartDate().isAfter(segmentStart)) {
			LocalDate end = segmentStart.plusDays(context.getMinDays()).minusDays(1);
			fastestCandidate = new PeriodCandidate(segmentStart, end, context.getMinDays(), CandidateType.FASTEST);
		}

		// C 후보 갱신
		LocalDate bestStartDate = null;
		int bestWeekendCount = -1;

		// C-1. 현재 free 구간에서 주말이 가장 많은 기간을 찾음 (최적 window 탐색)
		for (LocalDate start = segmentStart; !start.plusDays(context.getMinDays() - 1)
				.isAfter(segmentEnd); start = start.plusDays(1)) {
			
			LocalDate end = start.plusDays(context.getMinDays()).minusDays(1);
			int weekendCount = DateRangeUtils.countWeekend(start, end);

			if (weekendCount > bestWeekendCount) {
				bestWeekendCount = weekendCount;
				bestStartDate = start;
			} else if (weekendCount == bestWeekendCount) {
				if (bestStartDate == null || start.isBefore(bestStartDate)) {
					bestStartDate = start;
				}
			}
		}
		
		if(bestStartDate == null) {
			return;
		}
		LocalDate bestEndDate = bestStartDate.plusDays(context.getMinDays() - 1);
		
		// C-2. weekendCandidate랑 비교해서 갱신
		if(weekendCandidate == null) {
			weekendCandidate = new PeriodCandidate(bestStartDate, bestEndDate, context.getMinDays(), CandidateType.WEEKEND_OPTIMAL);
			return;
		}
		
		int currentWeekend = DateRangeUtils.countWeekend(weekendCandidate.getStartDate(), weekendCandidate.getEndDate());
		if (currentWeekend < bestWeekendCount || (currentWeekend == bestWeekendCount && weekendCandidate.getStartDate().isAfter(bestStartDate))) {
			weekendCandidate = new PeriodCandidate(bestStartDate, bestStartDate.plusDays(context.getMinDays() - 1), 
												context.getMinDays(), CandidateType.WEEKEND_OPTIMAL);
		}
	}

	
}
