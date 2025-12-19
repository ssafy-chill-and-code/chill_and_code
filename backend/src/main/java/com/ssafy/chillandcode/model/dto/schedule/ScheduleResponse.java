package com.ssafy.chillandcode.model.dto.schedule;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;

//프론트 캘린더/목록 화면에 내려주는 DTO
public class ScheduleResponse {
	private Long scheduleId;
	private String title;
	private ScheduleType scheduleType;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime startDateTime;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime endDateTime;

	public ScheduleResponse() {

	}

	public ScheduleResponse(Long scheduleId, String title, ScheduleType scheduleType, LocalDateTime startDateTime,
			LocalDateTime endDateTime) {
		super();
		this.scheduleId = scheduleId;
		this.title = title;
		this.scheduleType = scheduleType;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
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

	@Override
	public String toString() {
		return "ScheduleResponse [scheduleId=" + scheduleId + ", title=" + title + ", scheduleType=" + scheduleType
				+ ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + "]";
	}

}
