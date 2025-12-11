package com.ssafy.chillandcode.model.service;

import java.util.List;

import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleCreateRequest;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleResponse;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleUpdateRequest;

public interface ScheduleService {
	// 일정 생성
	boolean insertSchedule(ScheduleCreateRequest req);

	// 일정 조회
	List<ScheduleResponse> selectScheduleByMonth(long userId, String month, List<ScheduleType> type);

	// 일정 수정
	boolean updateSchedule(ScheduleUpdateRequest req);

	// 일정 삭제
	boolean deleteSchedule(long userId, long scheduleId);
}
