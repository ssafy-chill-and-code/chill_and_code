package com.ssafy.chillandcode.model.dto.schedule;

public class Schedule {
	private long scheduleId; // 일정 ID (PK)
	private long userId; // 사용자 ID (일정 소유자)
	private String title; // 일정 제목
	private ScheduleType scheduleType; // 일정 분류 (enum)
	private String startDateTime; // 일정 시작일
	private String endDateTime; // 일정 종료일
	private String createdAt; // 일정 생성일

	public static enum ScheduleType {
		PERSONAL, WORK, WORKATION;
	}

	public Schedule() {

	}

	public Schedule(long scheduleId, long userId, String title, ScheduleType scheduleType, String startDateTime,
			String endDateTime, String createdAt) {
		super();
		this.scheduleId = scheduleId;
		this.userId = userId;
		this.title = title;
		this.scheduleType = scheduleType;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.createdAt = createdAt;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
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

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Schedule [scheduleId=" + scheduleId + ", userId=" + userId + ", title=" + title + ", scheduleType="
				+ scheduleType + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", createdAt="
				+ createdAt + "]";
	}

}
