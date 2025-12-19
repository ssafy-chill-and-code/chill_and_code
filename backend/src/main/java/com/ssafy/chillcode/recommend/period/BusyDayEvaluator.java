package com.ssafy.chillcode.recommend.period;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.chillandcode.model.dto.schedule.Schedule.Tag;

public class BusyDayEvaluator {
	public Map<LocalDate, Boolean> calculateBusyDays(Map<LocalDate, List<Tag>> days,
			PeriodRecommendationContext context) {

		Map<LocalDate, Boolean> result = new HashMap<>();

		for (Map.Entry<LocalDate, List<Tag>> entry : days.entrySet()) {
			boolean isBusy = isBusyDay(entry.getValue(), context.isRemoteWorkAllowed(), context.isAllowLightPersonal());
			result.put(entry.getKey(), isBusy);
		}

		return result;
	}

	private boolean isBusyDay(List<Tag> tags, boolean remoteWorkAllowed, boolean allowLightPersonal) {
		// Busy-day rule (MVP)
		// 우선순위: HIGH_PRIORITY > ALL_DAY_EVENT > REMOTE_POSSIBLE > PERSONAL_FLEX >
		// UNKNOWN
		// HIGH_PRIORITY, ALL_DAY_EVENT -> 항상 busy
		// REMOTE_POSSIBLE, PERSONAL_FLEX -> 사용자 옵션에 따라

		if (tags == null || tags.isEmpty()) {
			return false; // free
		}

		if (tags.contains(Tag.HIGH_PRIORITY)) {
			return true;
		}

		if (tags.contains(Tag.ALL_DAY_EVENT)) {
			return true;
		}

		if (tags.contains(Tag.REMOTE_POSSIBLE)) {
			return !remoteWorkAllowed;
		}

		if (tags.contains(Tag.PERSONAL_FLEX)) {
			return !allowLightPersonal;
		}

		return !allowLightPersonal;
	}

}
