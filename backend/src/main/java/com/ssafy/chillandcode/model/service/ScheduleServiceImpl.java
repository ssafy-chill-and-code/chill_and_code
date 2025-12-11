package com.ssafy.chillandcode.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.ScheduleDao;
import com.ssafy.chillandcode.model.dto.schedule.Schedule;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleCreateRequest;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleResponse;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleUpdateRequest;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleDao scheduleDao;

	@Override
	public boolean insertSchedule(ScheduleCreateRequest req) {
		Schedule schedule = req.toEntity();
		return scheduleDao.insertSchedule(schedule) == 1;
	}

	@Override
	public List<ScheduleResponse> selectScheduleByMonth(long userId, String month) {
		List<Schedule> schedules = scheduleDao.selectScheduleByMonth(userId, month);

		return schedules.stream().map(s -> new ScheduleResponse(
				s.getScheduleId(), 
				s.getTitle(),
				s.getScheduleType(),
				s.getStartDateTime(), 
				s.getEndDateTime()
				)).collect(Collectors.toList());
	}

	@Override
	public boolean updateSchedule(ScheduleUpdateRequest req) {
		return scheduleDao.updateSchedule(req) == 1;
	}

	@Override
	public boolean deleteSchedule(long userId, long scheduleId) {
		return scheduleDao.deleteSchedule(userId, scheduleId) == 1;
	}

}
