package com.ssafy.chillandcode.model.dto.schedule;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;

/**
 * 월 단위 일정 분석 최종 응답 DTO
 *
 * - 분석 탭 UI가 이 DTO 하나로 구성되도록 설계 
 * - 추천 로직과 충돌하지 않는 "설명용 요약"
 */
public class ScheduleSummaryResponse {
	private YearMonth month;

	private AnalysisStatus overallStatus; // 월 전체 일정 패턴 요약 상태
	private int totalDays;
	private int mixedDayCount;
	private int flexibleDayCount;
	private int mostBusyWeek;	// 없으면 -1
	private List<Integer> recommendWeeks;
	private Map<ScheduleType, Double> scheduleTypeRatio;
	private String summaryComment;

	public ScheduleSummaryResponse() {

	}

	public ScheduleSummaryResponse(YearMonth month, AnalysisStatus overallStatus, int totalDays, int mixedDayCount,
			int flexibleDayCount, int mostBusyWeek, List<Integer> recommendWeeks,
			Map<ScheduleType, Double> scheduleTypeRatio, String summaryComment) {
		this.month = month;
		this.overallStatus = overallStatus;
		this.totalDays = totalDays;
		this.mixedDayCount = mixedDayCount;
		this.flexibleDayCount = flexibleDayCount;
		this.mostBusyWeek = mostBusyWeek;
		this.recommendWeeks = recommendWeeks;
		this.scheduleTypeRatio = scheduleTypeRatio;
		this.summaryComment = summaryComment;
	}
	
	//getter, setter
	public YearMonth getMonth() {
		return month;
	}

	public void setMonth(YearMonth month) {
		this.month = month;
	}

	public AnalysisStatus getOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(AnalysisStatus overallStatus) {
		this.overallStatus = overallStatus;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	public int getMixedDayCount() {
		return mixedDayCount;
	}

	public void setMixedDayCount(int mixedDayCount) {
		this.mixedDayCount = mixedDayCount;
	}

	public int getFlexibleDayCount() {
		return flexibleDayCount;
	}

	public void setFlexibleDayCount(int flexibleDayCount) {
		this.flexibleDayCount = flexibleDayCount;
	}

	public int getMostBusyWeek() {
		return mostBusyWeek;
	}

	public void setMostBusyWeek(int mostBusyWeek) {
		this.mostBusyWeek = mostBusyWeek;
	}

	public List<Integer> getRecommendWeeks() {
		return recommendWeeks;
	}

	public void setRecommendWeeks(List<Integer> recommendWeeks) {
		this.recommendWeeks = recommendWeeks;
	}

	public Map<ScheduleType, Double> getScheduleTypeRatio() {
		return scheduleTypeRatio;
	}

	public void setScheduleTypeRatio(Map<ScheduleType, Double> scheduleTypeRatio) {
		this.scheduleTypeRatio = scheduleTypeRatio;
	}

	public String getSummaryComment() {
		return summaryComment;
	}

	public void setSummaryComment(String summaryComment) {
		this.summaryComment = summaryComment;
	}

	@Override
	public String toString() {
		return "ScheduleSummaryResponse [month=" + month + ", overallStatus=" + overallStatus + ", totalDays="
				+ totalDays + ", mixedDayCount=" + mixedDayCount + ", flexibleDayCount=" + flexibleDayCount
				+ ", mostBusyWeek=" + mostBusyWeek + ", recommendWeeks=" + recommendWeeks + ", scheduleTypeRatio="
				+ scheduleTypeRatio + ", summaryComment=" + summaryComment + "]";
	}


}
