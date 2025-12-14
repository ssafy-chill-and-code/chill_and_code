package com.ssafy.chillcode.recommend.period;

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
	 * 기간 추천(분석) 흐름 
	 * 0. 특정 기간 일정 리스트를 넘겨받음 
	 * 1. 날짜 단위 flatten + tag 수집 
	 * 2. 옵션 반영해 busy/free 판정
	 * 3. 연속 free 기간 추출 (min~max) 
	 * 4. scoring + TOP1 선정
	 */
	
	public void recommend(List<Schedule> schedules, PeriodRecommendationContext context) {

		// 1. 날짜 단위 flatten + 태그 수집
		Map<LocalDate, List<Tag>> days = flattenSchedules(schedules, context);

		// 2. 옵션 반영해 busy/free 판정
		Map<LocalDate, Boolean> busyMap = calculateBusyDays(days, context);

		// 3. 연속 free 기간 추출
		findFreeSegments(busyMap, context);
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
	 * 2. 옵션 반영해 busy/free 판정
	 */
	public Map<LocalDate, Boolean> calculateBusyDays(Map<LocalDate, List<Tag>> days, PeriodRecommendationContext context) {

		Map<LocalDate, Boolean> result = new HashMap<>();
		
		for(Map.Entry<LocalDate, List<Tag>> entry : days.entrySet()) {
			boolean isBusy = isBusyDay(entry.getValue(), context.isRemoteWorkAllowed(), context.isAllowLightPersonal());
			result.put(entry.getKey(), isBusy);
		}
		
		return result;
	}

	private boolean isBusyDay(List<Tag> tags, boolean remoteWorkAllowed, boolean allowLightPersonal) {
		// Busy-day rule (MVP)
		// 우선순위: HIGH_PRIORITY > ALL_DAY_EVENT > REMOTE_POSSIBLE > PERSONAL_FLEX > UNKNOWN
		// HIGH_PRIORITY, ALL_DAY_EVENT -> 항상 busy
		// REMOTE_POSSIBLE, PERSONAL_FLEX -> 사용자 옵션에 따라
		
		if(tags == null || tags.isEmpty()) {
			return false; //free
		}
		
		if(tags.contains(Tag.HIGH_PRIORITY)) {
			return true;
		}
		
		if(tags.contains(Tag.ALL_DAY_EVENT)) {
			return true;
		}
		
		if(tags.contains(Tag.REMOTE_POSSIBLE)) {
			return !remoteWorkAllowed;
		}
		
		if(tags.contains(Tag.PERSONAL_FLEX)) {
			return !allowLightPersonal;
		}
		
		return !allowLightPersonal;
	}

	/**
	 * 3. 연속 free 기간 추출 (min~max)
	 */
	public List<FreeSegment> findFreeSegments(Map<LocalDate, Boolean> busyMap, PeriodRecommendationContext context) {
		List<FreeSegment> result = new ArrayList<>();
		
		boolean inFreeSegment = false;
		LocalDate segmentStart = null;
		LocalDate segmentEnd = null;
		
		//realStart, realEnd 값을 기준으로 날짜에 plus 1 씩하면서 검색을 통한 순회
		for(LocalDate d = context.getSearchStart(); !d.isAfter(context.getSearchEnd()); d = d.plusDays(1)) {
			
			//free 날짜
			if(!busyMap.getOrDefault(d, false)) {
				if(!inFreeSegment) {
					inFreeSegment = true;
					segmentStart = d;
				}
			}
			//busy 날짜
			else {
				if(inFreeSegment) {
					inFreeSegment = false;
					segmentEnd = d.minusDays(1);
					
					onFreeSegmentEnd(segmentStart, segmentEnd, context);
				}
			}
		}
		
		if(inFreeSegment) {
			onFreeSegmentEnd(segmentStart, context.getSearchEnd(), context);
		}
		return result;
	}
	
	//free 구간 종료 로직
	private void onFreeSegmentEnd(LocalDate segmentStart, LocalDate segmentEnd, PeriodRecommendationContext context) {
		int duration = (int) segmentStart.until(segmentEnd, ChronoUnit.DAYS) + 1;
		
		if(duration >= context.getMinDays()) {
			//후보 생성 정책 작성
		}
	}
}
