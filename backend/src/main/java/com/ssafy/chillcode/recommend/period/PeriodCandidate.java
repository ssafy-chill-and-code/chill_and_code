package com.ssafy.chillcode.recommend.period;

import java.time.LocalDate;

public class PeriodCandidate {
	
	LocalDate startDate;
	LocalDate endDate;
	int durationDays;
	CandidateType type;
	
	public PeriodCandidate() {

	}

	public PeriodCandidate(LocalDate startDate, LocalDate endDate, int durationDays, CandidateType type) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.durationDays = durationDays;
		this.type = type;
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

	public CandidateType getType() {
		return type;
	}

	public void setType(CandidateType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PeriodCandidate [startDate=" + startDate + ", endDate=" + endDate + ", durationDays=" + durationDays
				+ ", type=" + type + "]";
	}
	
}
