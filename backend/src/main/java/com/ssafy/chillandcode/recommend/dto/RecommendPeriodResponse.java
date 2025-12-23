package com.ssafy.chillandcode.recommend.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ssafy.chillandcode.recommend.period.PeriodCandidate;
import com.ssafy.chillandcode.recommend.period.PeriodRecommendationResult;

public class RecommendPeriodResponse {

	Period primary;
	String reason;
	List<Period> alternatives;
	
	private static final String NO_RESULT_MESSAGE = "추천 가능한 기간이 없습니다.";
	
	public RecommendPeriodResponse() {

	}

	public RecommendPeriodResponse(Period primary, String reason, List<Period> alternatives) {
		super();
		this.primary = primary;
		this.reason = reason;
		this.alternatives = alternatives;
	}
	
	//추천 불가 케이스 반환용
	public static RecommendPeriodResponse empty(String reason) {
		return new RecommendPeriodResponse(null, reason, Collections.emptyList());
	}
	
	//엔진 결과를 API DTO로 변환용
	public static RecommendPeriodResponse from(PeriodRecommendationResult result) {
		if(result == null || result.getPrimary() == null) {
			return empty("NO_RESULT_MESSAGE");
		}
		
		Period primary = Period.from(result.getPrimary());
		
		List<Period> alternatives;
		
		if(result.getAlternatives() == null) {
			alternatives = Collections.emptyList();
		}else {
			alternatives = new ArrayList<>();
			for(PeriodCandidate c : result.getAlternatives()) {
				Period p = Period.from(c);
				alternatives.add(p);
			}
		}
		
		return new RecommendPeriodResponse(primary, result.getPrimaryReason(), alternatives);
	}
	
	//getter, setter
	public Period getPrimary() {
		return primary;
	}

	public void setPrimary(Period primary) {
		this.primary = primary;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<Period> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(List<Period> alternatives) {
		this.alternatives = alternatives;
	}

	@Override
	public String toString() {
		return "RecommendPeriodResponse [primary=" + primary + ", reason=" + reason + ", alternatives=" + alternatives
				+ "]";
	}
	
}
