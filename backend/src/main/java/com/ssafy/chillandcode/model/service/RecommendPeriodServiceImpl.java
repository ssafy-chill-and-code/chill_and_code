package com.ssafy.chillandcode.model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.ScheduleDao;
import com.ssafy.chillandcode.model.dto.schedule.Schedule;
import com.ssafy.chillcode.recommend.UserStyle;
import com.ssafy.chillcode.recommend.dto.RecommendPeriodRequest;
import com.ssafy.chillcode.recommend.dto.RecommendPeriodResponse;
import com.ssafy.chillcode.recommend.period.PeriodCandidate;
import com.ssafy.chillcode.recommend.period.PeriodRecommendationContext;
import com.ssafy.chillcode.recommend.period.PeriodRecommendationEngine;
import com.ssafy.chillcode.recommend.period.PeriodRecommendationResult;
import com.ssafy.chillcode.recommend.period.PeriodScoringEngine;
import com.ssafy.chillcode.recommend.period.SearchWindow;

/**
 * 1. userId + search window로 일정 조회 
 * 2. 추천 context 생성(기간범위, 옵션) 
 * 3. 후보 생성 엔진 호출 
 * 4. 스코어링 엔진 호출 
 * 5. 결과 반환
 */

@Service
public class RecommendPeriodServiceImpl implements RecommendPeriodService {

	@Autowired
	private ScheduleDao scheduleDao;

	// 외부노출 서비스 메서드
	public RecommendPeriodResponse recommendPeriod(long userId, RecommendPeriodRequest req) {
		
		if(req == null) {
			req = new RecommendPeriodRequest();
		}
		
		LocalDate today = LocalDate.now();
		UserStyle style = resolveStyle(req);
		
		SearchWindow window = determineSearchWindow(today);

		List<Schedule> schedules = loadSchedules(userId, window);

		PeriodRecommendationContext context = buildContext(req, window, today, style);
		
		List<PeriodCandidate> candidates = generateCandidates(schedules, context);
		
		PeriodRecommendationResult recommendation = scoreCandidates(candidates, context, style);
		
		
		if(candidates.isEmpty() || recommendation == null || recommendation.getPrimary() == null) {
			return RecommendPeriodResponse.empty("추천 가능한 기간이 없습니다.");
		}
		
		return RecommendPeriodResponse.from(recommendation);
	}

	// search 범위 설정
	private SearchWindow determineSearchWindow(LocalDate today) {
		LocalDate start = today;
		
		// 추천 탐색 범위는 오늘~3개월 (MVP 기준) 추후 style/유저 설정으로 확장 가능
		LocalDate end = start.plusMonths(3); 


		return new SearchWindow(start, end);
	}

	// 일정 조회
	private List<Schedule> loadSchedules(long userId, SearchWindow window) {
		
		LocalDateTime start = window.getStart().atStartOfDay();
		LocalDateTime end = window.getEnd().atTime(23, 59, 59);
		
		return scheduleDao.selectScheduleByRange(userId, start, end);
	}

	// 추천 context 생성
	private PeriodRecommendationContext buildContext(RecommendPeriodRequest req, SearchWindow window, LocalDate today, UserStyle style) {
		
		LocalDate searchStart = window.getStart();
		LocalDate searchEnd = window.getEnd();
		
		int minDays = req.getMinDays() != null ? req.getMinDays() : 3;
		int maxDays = req.getMaxDays() != null ? req.getMaxDays() : style.getLengthCap();
		
		// MVP 정책: 개인 일정은 기본적으로 free로 간주. (추후 allowLightPersonal 옵션 UI 노출 가능)
		boolean remoteWorkAllowed = req.isRemoteWorkAllowed();
		boolean allowLightPersonal = true;
		
		return new PeriodRecommendationContext(searchStart, searchEnd, minDays, maxDays, remoteWorkAllowed, allowLightPersonal, today);

	}
	
	
	//null 방지
	private UserStyle resolveStyle(RecommendPeriodRequest req) {
		if(req != null && req.getStyle() != null) {
			return req.getStyle();
		}
		return UserStyle.BALANCE;
	}

	// 후보 생성
	private List<PeriodCandidate> generateCandidates(List<Schedule> schedules, PeriodRecommendationContext context) {
		
		PeriodRecommendationEngine engine = new PeriodRecommendationEngine();
		
		List<PeriodCandidate> result = engine.recommend(schedules, context);
		
		return result;

	}

	// 스코어링 엔진 호출
	private PeriodRecommendationResult scoreCandidates(List<PeriodCandidate> candidates,
			PeriodRecommendationContext context, UserStyle style) {
		
		PeriodScoringEngine engine = new PeriodScoringEngine();
		
		PeriodRecommendationResult result = engine.recommend(candidates, context.getToday(), style);
		
		return result;

	}
}
