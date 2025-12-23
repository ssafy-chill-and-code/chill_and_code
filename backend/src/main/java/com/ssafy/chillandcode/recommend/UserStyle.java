package com.ssafy.chillandcode.recommend;

import java.util.List;

import com.ssafy.chillandcode.recommend.period.CandidateType;

public enum UserStyle {
	RELAX(	
			"휴양형",
			14,
			List.of(
					CandidateType.LONGEST,
					CandidateType.WEEKEND_OPTIMAL,
					CandidateType.FASTEST
			)
		), // 휴양형
	WORK(
			"업무형",
			7,  
			List.of(
					CandidateType.FASTEST,
					CandidateType.LONGEST,
					CandidateType.WEEKEND_OPTIMAL
			)
		), // 업무형
	BALANCE(
			"밸런스형",
			10,
			List.of(
					CandidateType.LONGEST,
					CandidateType.FASTEST,
					CandidateType.WEEKEND_OPTIMAL
			)
		); // 밸런스형
	
	private final String description;
	private final int lengthCap;
	private final List<CandidateType> priority;
	
	UserStyle(String description, int lengthCap, List<CandidateType> priority){
		this.description = description;
		this.lengthCap = lengthCap;
		this.priority = priority;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getLengthCap() {
		return lengthCap;
	}
	
	public List<CandidateType> getPriority() {
		return priority;
	}
}
