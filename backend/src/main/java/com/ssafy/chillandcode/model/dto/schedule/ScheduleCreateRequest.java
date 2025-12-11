package com.ssafy.chillandcode.model.dto.schedule;

import java.time.LocalDateTime;

import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;

public class ScheduleCreateRequest {
	private long userId; // 일정 소유자 (Security 도입 시 제거 가능)
	private String title; // 일정 제목
	private ScheduleType scheduleType; // 일정 분류 (enum)
	private LocalDateTime startDateTime; // 일정 시작일
	private LocalDateTime endDateTime; // 일정 종료일
	private String userTag; // (선택) 사용자가 직접 지정한 태그

	public ScheduleCreateRequest() {

	}

	public ScheduleCreateRequest(long userId, String title, ScheduleType scheduleType, LocalDateTime startDateTime,
			LocalDateTime endDateTime, String userTag) {
		super();
		this.userId = userId;
		this.title = title;
		this.scheduleType = scheduleType;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.userTag = userTag;
	}
	
	public Schedule toEntity() {
		Schedule schedule = new Schedule();
		schedule.setUserId(userId);
		schedule.setTitle(title);
		schedule.setScheduleType(scheduleType);
		schedule.setStartDateTime(startDateTime);
		schedule.setEndDateTime(endDateTime);
		schedule.setUserTag(userTag);
		return schedule;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ScheduleType getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(ScheduleType scheduleType) {
		this.scheduleType = scheduleType;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getUserTag() {
		return userTag;
	}

	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}

	@Override
	public String toString() {
		return "ScheduleCreateRequest [userId=" + userId + ", title=" + title + ", scheduleType=" + scheduleType
				+ ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", userTag=" + userTag + "]";
	}

}
