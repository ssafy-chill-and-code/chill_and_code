package com.ssafy.chillcode.model.engine.period;

import java.time.LocalDate;
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

	public void recommend(List<Schedule> schedules, LocalDate searchStart, LocalDate searchEnd, boolean remoteWorkAllowed,
		    boolean allowLightPersonal) {
		// 1. 날짜 단위 flatten + 태그 수집
		// 여기서는 그냥 Tag 정리하지 않고 다 모아둠
		Map<LocalDate, List<Tag>> days = flattenSchedules(schedules, searchStart, searchEnd);

		// 2. 옵션 반영해 busy/free 판정
		// 이 단계에서 Tag 우선순위 적용
		Map<LocalDate, Boolean> busyMap = calculateBusyDays(days, remoteWorkAllowed, allowLightPersonal);

		// 3. 연속 free 기간 추출
		findFreeSegments(busyMap);
	}

	/**
	 * 1. 날짜 단위 flatten + tag 수집
	 */
	public Map<LocalDate, List<Tag>> flattenSchedules(List<Schedule> schedules, LocalDate searchStart,
			LocalDate searchEnd) {

		Map<LocalDate, List<Tag>> result = new HashMap<>();

		for (Schedule s : schedules) {

			// autoTag랑 userTag 중에서 뽑아올 Tag 결정
			Tag tag = resolveFinalTag(s);

			// 날짜 변환 LocalDateTime -> LocalDate
			LocalDate start = s.getStartDateTime().toLocalDate();
			LocalDate end = s.getEndDateTime().toLocalDate();

			// 기간 추천 시 고려할 일정 범위를 벗어나는지 체크
			// 일정이 검색 범위에 걸쳐 있는 경우, 실제 추천 대상 날짜만 잘라서 사용한다
			LocalDate realStart = start.isBefore(searchStart) ? searchStart : start;
			LocalDate realEnd = end.isAfter(searchEnd) ? searchEnd : end;

			// 순회하면서 Map에 기간 추가하고 Tag 추가하기
			for (LocalDate d = realStart; !d.isAfter(realEnd); d = d.plusDays(1)) {
				// 날짜별 Tag 리스트 누적
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
	public Map<LocalDate, Boolean> calculateBusyDays(Map<LocalDate, List<Tag>> days, boolean remoteWorkAllowed, boolean allowLightPersonal) {

		Map<LocalDate, Boolean> result = new HashMap<>();
		
		for(Map.Entry<LocalDate, List<Tag>> entry : days.entrySet()) {
			boolean isBusy = isBusyDay(entry.getValue(), remoteWorkAllowed, allowLightPersonal);
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
	public void findFreeSegments(Map<LocalDate, Boolean> busyMap) {
		// Map을 순회?
	}
}
