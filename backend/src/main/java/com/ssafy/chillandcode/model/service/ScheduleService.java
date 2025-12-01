package com.ssafy.chillandcode.model.service;

import java.util.List;

import com.ssafy.chillandcode.model.dto.Schedule;

public interface ScheduleService {
	// 일정 생성
	boolean insertSchedule(Schedule schedule);

	// 일정 조회
	List<Schedule> selectScheduleByMonth(long userId, String month);

	// 일정 수정
	boolean updateSchedule(long userId, long scheduleId, Schedule schedule);

	// 일정 삭제
	boolean deleteSchedule(long userId, long scheduleId);
}
