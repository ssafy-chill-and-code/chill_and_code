package com.ssafy.chillandcode.model.dto.schedule;

import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;

public class ScheduleUpdateRequest {
	private long scheduleId; // 수정할 일정
	private long userId;
	private String title;
	private String startDateTime;
	private String endDateTime;
	private ScheduleType scheduleType;
	private String userTag;

	public ScheduleUpdateRequest() {

	}

	public ScheduleUpdateRequest(long scheduleId, String title, String startDateTime, String endDateTime,
			ScheduleType scheduleType, String userTag) {
		super();
		this.scheduleId = scheduleId;
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.scheduleType = scheduleType;
		this.userTag = userTag;
	}

	public ScheduleUpdateRequest(long scheduleId, long userId, String title, String startDateTime, String endDateTime,
			ScheduleType scheduleType, String userTag) {
		super();
		this.scheduleId = scheduleId;
		this.userId = userId;
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.scheduleType = scheduleType;
		this.userTag = userTag;
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

	public ScheduleType getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(ScheduleType scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getUserTag() {
		return userTag;
	}

	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}

	@Override
	public String toString() {
		return "ScheduleUpdateRequest [scheduleId=" + scheduleId + ", userId=" + userId + ", title=" + title
				+ ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", scheduleType=" + scheduleType
				+ ", userTag=" + userTag + "]";
	}

}
