package com.ssafy.chillandcode.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.ScheduleDao;
import com.ssafy.chillandcode.model.dto.schedule.Schedule;

@Service
public class ScheduleServiceImpl implements ScheduleService{
	
	@Autowired
	private ScheduleDao scheduleDao;
	
	@Override
	public boolean insertSchedule(Schedule schedule) {
		return scheduleDao.insertSchedule(schedule) == 1;
	}

	@Override
	public List<Schedule> selectScheduleByMonth(long userId, String month) {
		return scheduleDao.selectScheduleByMonth(userId, month);
	}

	@Override
	public boolean updateSchedule(long userId, long scheduleId, Schedule schedule) {
		return scheduleDao.updateSchedule(userId, scheduleId, schedule) == 1;
	}

	@Override
	public boolean deleteSchedule(long userId, long scheduleId) {
		return scheduleDao.deleteSchedule(userId, scheduleId) == 1;
	}

	
}
