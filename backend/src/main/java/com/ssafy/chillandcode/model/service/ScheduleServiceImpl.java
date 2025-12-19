package com.ssafy.chillandcode.model.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dao.ScheduleDao;
import com.ssafy.chillandcode.model.dto.schedule.Schedule;
import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleCreateRequest;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleResponse;
import com.ssafy.chillandcode.model.dto.schedule.ScheduleUpdateRequest;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleDao scheduleDao;

	// 일정 등록
	@Override
	public void insertSchedule(ScheduleCreateRequest req) {
		
		if(req.getUserId() == null) {
			throw new ApiException(ErrorCode.UNAUTHORIZED);
		}
		
		if(req.getStartDateTime().isAfter(req.getEndDateTime())) {
			throw new ApiException(ErrorCode.INVALID_SCHEDULE_TIME);
		}
		
		int rows = scheduleDao.insertSchedule(req.toEntity());
		if(rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "일정 등록에 실패했습니다.");
		}
	}

	// 일정 월별 조회
	@Override
    public List<ScheduleResponse> selectScheduleByMonth(Long userId, String month, List<ScheduleType> type) {
		List<Schedule> schedules = scheduleDao.selectScheduleByMonth(userId, month, type);

		return schedules.stream().map(s -> new ScheduleResponse(s.getScheduleId(), s.getTitle(), s.getScheduleType(),
				s.getStartDateTime(), s.getEndDateTime())).collect(Collectors.toList());
	}

	// 일정 범위 조회
	@Override
    public List<Schedule> findScheduleByRange(Long userId, LocalDateTime start, LocalDateTime end) {
		return scheduleDao.selectScheduleByRange(userId, start, end);
	}

	// 일정 수정
	@Override
	public void updateSchedule(Long userId, ScheduleUpdateRequest req) {

		Schedule target = scheduleDao.selectById(req.getScheduleId());
		if (target == null) {
			throw new ApiException(ErrorCode.SCHEDULE_NOT_FOUND);
		}

		if (!target.getUserId().equals(userId)) {
			throw new ApiException(ErrorCode.FORBIDDEN_ACTION, "본인의 일정만 수정할 수 있습니다.");
		}

		int rows = scheduleDao.updateSchedule(req);
		if (rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "일정 수정에 실패했습니다.");
		}
	}

	// 일정 삭제
	@Override
	public void deleteSchedule(Long userId, Long scheduleId) {

		Schedule target = scheduleDao.selectById(scheduleId);
		if (target == null) {
			throw new ApiException(ErrorCode.SCHEDULE_NOT_FOUND);
		}

		if (!target.getUserId().equals(userId)) {
			throw new ApiException(ErrorCode.FORBIDDEN_ACTION, "본인의 일정만 삭제할 수 있습니다.");
		}

		int rows = scheduleDao.deleteSchedule(userId, scheduleId);
		if (rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "일정 삭제에 실패했습니다.");
		}
	}

}
