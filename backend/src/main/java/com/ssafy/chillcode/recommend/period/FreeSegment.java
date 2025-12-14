package com.ssafy.chillcode.recommend.period;

import java.time.LocalDate;

public class FreeSegment {
	LocalDate startDate;
	LocalDate endDate;
	int durationDays; // 연속 기간(endDate - StartDate + 1)

	public FreeSegment() {

	}

	public FreeSegment(LocalDate startDate, LocalDate endDate, int durationDays) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.durationDays = durationDays;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}

	@Override
	public String toString() {
		return "FreeSegment [startDate=" + startDate + ", endDate=" + endDate + ", durationDays=" + durationDays + "]";
	}

}
