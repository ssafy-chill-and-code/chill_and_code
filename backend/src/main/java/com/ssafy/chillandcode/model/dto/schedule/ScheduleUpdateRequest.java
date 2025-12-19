package com.ssafy.chillandcode.model.dto.schedule;

import java.time.LocalDateTime;

import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;
import com.ssafy.chillandcode.model.dto.schedule.Schedule.Tag;

public class ScheduleUpdateRequest {
	private long scheduleId; // 수정할 일정
	private long userId;
	private String title;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private ScheduleType scheduleType;
	private Tag userTag;

	public ScheduleUpdateRequest() {

	}

	public ScheduleUpdateRequest(long scheduleId, long userId, String title, LocalDateTime startDateTime,
			LocalDateTime endDateTime, ScheduleType scheduleType, Tag userTag) {
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

	public ScheduleType getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(ScheduleType scheduleType) {
		this.scheduleType = scheduleType;
	}

	public Tag getUserTag() {
		return userTag;
	}

	public void setUserTag(Tag userTag) {
		this.userTag = userTag;
	}

	@Override
	public String toString() {
		return "ScheduleUpdateRequest [scheduleId=" + scheduleId + ", userId=" + userId + ", title=" + title
				+ ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", scheduleType=" + scheduleType
				+ ", userTag=" + userTag + "]";
	}

}
