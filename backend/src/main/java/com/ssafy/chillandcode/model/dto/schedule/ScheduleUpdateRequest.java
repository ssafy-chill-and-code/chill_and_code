package com.ssafy.chillandcode.model.dto.schedule;

import java.time.LocalDateTime;

import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;
import com.ssafy.chillandcode.model.dto.schedule.Schedule.Tag;

public class ScheduleUpdateRequest {
	private Long scheduleId; // 수정할 일정
	private String title;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private ScheduleType scheduleType;
	private Tag userTag;

	public ScheduleUpdateRequest() {

	}

	public ScheduleUpdateRequest(Long scheduleId, String title, LocalDateTime startDateTime,
			LocalDateTime endDateTime, ScheduleType scheduleType, Tag userTag) {
		super();
		this.scheduleId = scheduleId;
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.scheduleType = scheduleType;
		this.userTag = userTag;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
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
		return "ScheduleUpdateRequest [scheduleId=" + scheduleId + ", title=" + title
				+ ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", scheduleType=" + scheduleType
				+ ", userTag=" + userTag + "]";
	}

}
