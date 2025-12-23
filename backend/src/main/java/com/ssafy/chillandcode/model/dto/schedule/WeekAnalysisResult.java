package com.ssafy.chillandcode.model.dto.schedule;

/**
 * 주 단위 일정 분석 결과 DTO
 *
 * - 가장 바쁜 주 / 여유로운 주 계산용 
 * - UI의 "주 단위 신호" 영역에 사용
 */

public class WeekAnalysisResult {
	private int weekOfMonth;
	private int busyDayCount;
	private int flexibleDayCount; //MIXED는 주 판단의 핵심이 아니므로 포함하지 않음

	public WeekAnalysisResult() {

	}

	public WeekAnalysisResult(int weekOfMonth, int busyDayCount, int flexibleDayCount) {
		this.weekOfMonth = weekOfMonth;
		this.busyDayCount = busyDayCount;
		this.flexibleDayCount = flexibleDayCount;
	}

	public int getWeekOfMonth() {
		return weekOfMonth;
	}

	public void setWeekOfMonth(int weekOfMonth) {
		this.weekOfMonth = weekOfMonth;
	}

	public int getBusyDayCount() {
		return busyDayCount;
	}

	public void setBusyDayCount(int busyDayCount) {
		this.busyDayCount = busyDayCount;
	}

	public int getFlexibleDayCount() {
		return flexibleDayCount;
	}

	public void setFlexibleDayCount(int flexibleDayCount) {
		this.flexibleDayCount = flexibleDayCount;
	}

	@Override
	public String toString() {
		return "WeekAnalysisResult [weekOfMonth=" + weekOfMonth + ", busyDayCount=" + busyDayCount
				+ ", flexibleDayCount=" + flexibleDayCount + "]";
	}

}
