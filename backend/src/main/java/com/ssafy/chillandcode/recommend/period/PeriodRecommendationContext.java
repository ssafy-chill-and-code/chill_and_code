package com.ssafy.chillandcode.recommend.period;

import java.time.LocalDate;

//읽기 전용 객체
public class PeriodRecommendationContext {
	// 기간 추천의 범위 기준
	LocalDate searchStart;
	LocalDate searchEnd;

	// 사용자 기간 제약 조건
	int minDays;
	int maxDays;

	// Busy/Free 판정에 영향을 주는 사용자 옵션
	boolean remoteWorkAllowed;
	boolean allowLightPersonal; //true default 값

	// 추천 기준 시점
	LocalDate today;

	public PeriodRecommendationContext() {

	}

	public PeriodRecommendationContext(LocalDate searchStart, LocalDate searchEnd, int minDays, int maxDays,
			boolean remoteWorkAllowed, boolean allowLightPersonal, LocalDate today) {
		super();
		this.searchStart = searchStart;
		this.searchEnd = searchEnd;
		this.minDays = minDays;
		this.maxDays = maxDays;
		this.remoteWorkAllowed = remoteWorkAllowed;
		this.allowLightPersonal = allowLightPersonal;
		this.today = today;
	}
	
	//분석 전용 팩토리 메서드
	public static PeriodRecommendationContext forAnalysis() {
	    return new PeriodRecommendationContext(
	        null,               // searchStart: 분석에서는 사용 안 함
	        null,               // searchEnd
	        0,                  // minDays
	        Integer.MAX_VALUE,  // maxDays (분석에서는 제약 없음)
	        true,               // remoteWorkAllowed: 관대하게
	        true,               // allowLightPersonal: 관대하게
	        LocalDate.now()     // today
	    );
	}
	
	//getter, setter
	public LocalDate getSearchStart() {
		return searchStart;
	}

	public LocalDate getSearchEnd() {
		return searchEnd;
	}

	public int getMinDays() {
		return minDays;
	}

	public int getMaxDays() {
		return maxDays;
	}

	public boolean isRemoteWorkAllowed() {
		return remoteWorkAllowed;
	}

	public boolean isAllowLightPersonal() {
		return allowLightPersonal;
	}

	public LocalDate getToday() {
		return today;
	}

	@Override
	public String toString() {
		return "PeriodRecommendationContext [searchStart=" + searchStart + ", searchEnd=" + searchEnd + ", minDays="
				+ minDays + ", maxDays=" + maxDays + ", remoteWorkAllowed=" + remoteWorkAllowed
				+ ", allowLightPersonal=" + allowLightPersonal + ", today=" + today + "]";
	}

}
