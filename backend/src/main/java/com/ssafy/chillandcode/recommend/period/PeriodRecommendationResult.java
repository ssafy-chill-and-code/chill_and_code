package com.ssafy.chillandcode.recommend.period;

import java.util.List;

/**
 * 엔진 최종 산출물*/
public class PeriodRecommendationResult {

	PeriodCandidate primary; // TOP1
	String primaryReason; // 선택 이유

	List<PeriodCandidate> alternatives; // Secondary options

	public PeriodRecommendationResult() {

	}

	public PeriodRecommendationResult(PeriodCandidate primary, String primaryReason,
			List<PeriodCandidate> alternatives) {
		super();
		this.primary = primary;
		this.primaryReason = primaryReason;
		this.alternatives = alternatives;
	}

	public PeriodCandidate getPrimary() {
		return primary;
	}

	public void setPrimary(PeriodCandidate primary) {
		this.primary = primary;
	}

	public String getPrimaryReason() {
		return primaryReason;
	}

	public void setPrimaryReason(String primaryReason) {
		this.primaryReason = primaryReason;
	}

	public List<PeriodCandidate> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(List<PeriodCandidate> alternatives) {
		this.alternatives = alternatives;
	}

	@Override
	public String toString() {
		return "PeriodRecommendationResult [primary=" + primary + ", primaryReason=" + primaryReason + ", alternatives="
				+ alternatives + "]";
	}

}
