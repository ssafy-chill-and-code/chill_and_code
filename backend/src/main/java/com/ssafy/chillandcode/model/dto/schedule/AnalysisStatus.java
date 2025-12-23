package com.ssafy.chillandcode.model.dto.schedule;

/**
 * 일정 분석 전용 상태 Enum
 * 상태 의미:
 * BUSY      : 추천 로직 기준으로 이동/체류가 불가능한 날 (차단)
 * MIXED     : 추천은 가능하나, 일정 밀도나 패턴상 부담이 있는 날
 * FLEXIBLE  : 일정이 가볍거나 없어 이동/체류에 매우 유연한 날
 * */
public enum AnalysisStatus {
	// 추천 로직 기준 차단된 날 (HIGH_PRIORITY, ALL_DAY_EVENT 등)
	BUSY, 
	
	// 추천은 가능하나, 연속 일정/높은 밀도로 부담이 있는 날
	MIXED,
	
	// 일정이 가볍거나 없어 워케이션에 매우 적합한 날
	FLEXIBLE
}
