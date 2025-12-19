package com.ssafy.chillandcode.model.service;

import java.time.LocalDateTime;
import java.util.List;

import com.ssafy.chillandcode.model.dto.schedule.Schedule;
import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleCreateRequest;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleResponse;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleUpdateRequest;

public interface ScheduleService {
	// 일정 생성
	void insertSchedule(ScheduleCreateRequest req);

	// 일정 조회
    List<ScheduleResponse> selectScheduleByMonth(Long userId, String month, List<ScheduleType> type);

	// 일정 수정
	void updateSchedule(Long userId, ScheduleUpdateRequest req);

	// 일정 삭제
	void deleteSchedule(Long userId, Long scheduleId);
	
	// 기간 조회
    List<Schedule> findScheduleByRange(Long userId, LocalDateTime start, LocalDateTime end);
}
