package com.ssafy.chillandcode.model.dto.schedule;

import java.time.LocalDateTime;

public class Schedule {
	private Long scheduleId; // 일정 ID (PK)
	private Long userId; // 사용자 ID (일정 소유자)
	private String title; // 일정 제목
	private ScheduleType scheduleType; // 일정 분류 (enum)
	private LocalDateTime startDateTime; // 일정 시작일
	private LocalDateTime endDateTime; // 일정 종료일
	private String createdAt; // 일정 생성일
	private Tag autoTag; // 시스템이 추론한 성격(override)
	private Tag userTag; // 사용자 태그
	private String updatedAt;

	public static enum ScheduleType {
		PERSONAL, WORK, WORKATION;
	}

	public static enum Tag {
		HIGH_PRIORITY, REMOTE_POSSIBLE, PERSONAL_FLEX, ALL_DAY_EVENT, UNKNOWN;
	}

	public Schedule() {

	}

	public Schedule(Long scheduleId, Long userId, String title, ScheduleType scheduleType, LocalDateTime startDateTime,
			LocalDateTime endDateTime, String createdAt, Tag autoTag, Tag userTag, String updatedAt) {
		super();
		this.scheduleId = scheduleId;
		this.userId = userId;
		this.title = title;
		this.scheduleType = scheduleType;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.createdAt = createdAt;
		this.autoTag = autoTag;
		this.userTag = userTag;
		this.updatedAt = updatedAt;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public Tag getAutoTag() {
		return autoTag;
	}

	public void setAutoTag(Tag autoTag) {
		this.autoTag = autoTag;
	}

	public Tag getUserTag() {
		return userTag;
	}

	public void setUserTag(Tag userTag) {
		this.userTag = userTag;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Schedule [scheduleId=" + scheduleId + ", userId=" + userId + ", title=" + title + ", scheduleType="
				+ scheduleType + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", createdAt="
				+ createdAt + ", autoTag=" + autoTag + ", userTag=" + userTag + ", updatedAt=" + updatedAt + "]";
	}

}
