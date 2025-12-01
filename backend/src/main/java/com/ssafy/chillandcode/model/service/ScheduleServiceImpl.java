package com.ssafy.chillandcode.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.ScheduleDao;
import com.ssafy.chillandcode.model.dto.Schedule;
import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;

@Service
public class ScheduleServiceImpl implements ScheduleService{
	
	@Autowired
	private ScheduleDao scheduleDao;
	
	@Override
    public boolean insertSchedule(Schedule schedule) {
		if (scheduleDao.insertSchedule(schedule) != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		return true;
    }

	@Override
	public List<Schedule> selectScheduleByMonth(long userId, String month) {
		return scheduleDao.selectScheduleByMonth(userId, month);
	}

	@Override
    public boolean updateSchedule(long userId, long scheduleId, Schedule schedule) {
		if (scheduleDao.updateSchedule(userId, scheduleId, schedule) != 1) {
			// 소유자 불일치 또는 미존재로 인한 갱신 실패를 통합 처리
			throw new ApiException(ErrorCode.SCHEDULE_NOT_FOUND);
		}
		return true;
    }

	@Override
    public boolean deleteSchedule(long userId, long scheduleId) {
		if (scheduleDao.deleteSchedule(userId, scheduleId) != 1) {
			throw new ApiException(ErrorCode.SCHEDULE_NOT_FOUND);
		}
		return true;
    }

	
}
