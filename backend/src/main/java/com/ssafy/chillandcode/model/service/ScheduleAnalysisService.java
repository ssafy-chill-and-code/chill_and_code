package com.ssafy.chillandcode.model.service;

import java.time.YearMonth;

import com.ssafy.chillandcode.model.dto.schedule.ScheduleSummaryResponse;

/**
 * 일정 분석(설명) 전용 서비스
 *
 * 핵심 원칙:
 * - 추천 로직(결정)을 대체하지 않고, 월 단위 일정 패턴을 "설명"한다.
 * - Controller는 분석 내부 계산(하루 단위 판단, 주/월 집계 등)을 몰라도 된다.
 *
 * 반환값(ScheduleSummaryResponse)은 프론트 분석 탭을 구성하기 위한 최종 DTO다.
 */
public interface ScheduleAnalysisService {
	 ScheduleSummaryResponse analyzeMonth(long userId, YearMonth month);
}
