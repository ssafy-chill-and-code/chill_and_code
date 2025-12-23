package com.ssafy.chillandcode.recommend.dto;

import java.time.LocalDate;

import com.ssafy.chillandcode.recommend.period.PeriodCandidate;

public class Period {
	LocalDate startDate;
	LocalDate endDate;
	int durationDays;

	public Period() {

	}

	public Period(LocalDate startDate, LocalDate endDate, int durationDays) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.durationDays = durationDays;
	}
	
	public static Period from(PeriodCandidate c) {
		return new Period(c.getStartDate(), c.getEndDate(), c.getDurationDays());
	}
	
	//getter, setter
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
		return "Period [startDate=" + startDate + ", endDate=" + endDate + ", durationDays=" + durationDays + "]";
	}

}
