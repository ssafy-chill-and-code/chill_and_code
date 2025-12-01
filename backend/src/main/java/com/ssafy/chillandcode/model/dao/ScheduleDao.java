package com.ssafy.chillandcode.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.chillandcode.model.dto.Schedule;

@Mapper
public interface ScheduleDao {
	
	//일정 생성
	int insertSchedule(Schedule schedule);
	
	//일정 조회
	List<Schedule> selectScheduleByMonth(
			@Param("userId") long userId, @Param("month") String month);
	
	//일정 수정
	int updateSchedule(
			@Param("userId") long userId, @Param("scheduleId") long scheduleId, Schedule schedule);
	
	//일정 삭제
	int deleteSchedule(
			@Param("userId") long userId, @Param("scheduleId") long scheduleId);
}
