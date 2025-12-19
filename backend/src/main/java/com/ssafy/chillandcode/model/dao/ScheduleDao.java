package com.ssafy.chillandcode.model.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.chillandcode.model.dto.schedule.Schedule;
import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleUpdateRequest;

@Mapper
public interface ScheduleDao {
	
	//일정 생성
	int insertSchedule(Schedule schedule);
	
	//일정 단건 조회
    Schedule selectById(@Param("scheduleId") Long scheduleId);
	
	//일정 조회
    List<Schedule> selectScheduleByMonth(
			@Param("userId") Long userId, @Param("month") String month, 
			@Param("type") List<ScheduleType> type);
	
	//일정 수정
	int updateSchedule(ScheduleUpdateRequest req);
	
	//일정 삭제
    int deleteSchedule(
			@Param("userId") Long userId, @Param("scheduleId") Long scheduleId);
	
	//기간 조회
    List<Schedule> selectScheduleByRange(
			@Param("userId") Long userId,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end
			);
	
}
