package com.ssafy.chillandcode.model.dto.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 하루 단위 일정 분석 결과 DTO
 *
 * - 월/주 분석의 최소 단위
 * - "왜 이 날이 이렇게 판단됐는지"를 설명하기 위한 구조
 */
public class DayAnalysisResult {
	private LocalDate date; // 분석 대상 날짜
	private AnalysisStatus status; // 분석 결과 상태(enum)
	private List<String> reasonTags = new ArrayList<>(); // 판단 근거 태그

	public DayAnalysisResult() {

	}

	public DayAnalysisResult(LocalDate date, AnalysisStatus status, List<String> reasonTags) {
		this.date = date;
		this.status = status;
		this.reasonTags = reasonTags;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public AnalysisStatus getStatus() {
		return status;
	}

	public void setStatus(AnalysisStatus status) {
		this.status = status;
	}

	public List<String> getReasonTags() {
		return reasonTags;
	}

	public void setReasonTags(List<String> reasonTags) {
		this.reasonTags = reasonTags;
	}

	@Override
	public String toString() {
		return "DayAnalysisResult [date=" + date + ", status=" + status + ", reasonTags=" + reasonTags + "]";
	}

}
