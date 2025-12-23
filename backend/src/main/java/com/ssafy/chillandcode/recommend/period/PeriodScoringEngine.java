package com.ssafy.chillandcode.recommend.period;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.chillandcode.recommend.UserStyle;

/**
 * 역할 - Engine이 만든 PeriodCandidate들을 입력으로 받는다 
 * - TOP1(Primary)을 결정한다 
 * - 의미가 다른 대안후보(Alternatives)를 함께 반환한다
 */

public class PeriodScoringEngine {
	private static final double EPSILON = 0.5; // score 차이가 이 값보다 작으면 의미없는 차이로 간주
	private static final double THRESHOLD = 0.25; // 후보 선정 threshold - primary 대비 이 비율 미만이면 대안으로 보여주지 않음
	
	private static class ScoredCandidate {
		PeriodCandidate candidate;
		double score;

		public ScoredCandidate(PeriodCandidate candidate, double score) {
			this.candidate = candidate;
			this.score = score;
		}

		public PeriodCandidate getCandidate() {
			return candidate;
		}

		public double getScore() {
			return score;
		}

	}
	
	public PeriodRecommendationResult recommend(List<PeriodCandidate> candidates, LocalDate today,
			UserStyle userStyle) {

		if (candidates.isEmpty()) {
			return null;
		}
		
		List<ScoredCandidate> scored = new ArrayList<>();

		// 점수 계산
		for (PeriodCandidate candidate : candidates) {
			double score = scoreCandidate(candidate, today, userStyle);
			scored.add(new ScoredCandidate(candidate, score));
		}
		
		//점수 기준 내림차순 정렬
		scored.sort(Comparator.comparingDouble(ScoredCandidate::getScore).reversed());

		// TOP1 선정
		PeriodCandidate primary = selectPrimary(scored, userStyle);

		// 대안 선정
		List<PeriodCandidate> alternatives = selectAlternatives(scored, primary);
		
		// 이유 생성
		String reason = generatePrimaryReason(primary, userStyle);
		
		//결과 반환
		PeriodRecommendationResult result = new PeriodRecommendationResult(primary, reason, alternatives);
		
		return result;
		
	}

	// 점수 계산
	// 이 후보가 TOIP1으로 선택될 정당성을 수치로 표현하면 얼마나 되는가
	private double scoreCandidate(PeriodCandidate candidate, LocalDate today, UserStyle userStyle) {
		/**
		 * 1. userStyle + candidateType -> baseScore
		 * 2. length, proximity, weekend 보정
		 * 3. 최종 score
		 * */
		
		//baseScore
		double score = getBaseScore(userStyle, candidate.getType());
		
		//공통 feature 계산
		int daysAway = (int) ChronoUnit.DAYS.between(today, candidate.getStartDate());
		int duration = candidate.getDurationDays();
		
		// CandidateType별 보정
		switch(candidate.getType()) {
			case LONGEST:
				int lengthScore = Math.min(duration, userStyle.getLengthCap());
				score += lengthScore * 2.0;
				score -= daysAway * 0.5;
				score = Math.max(score, 0);
				break;
			case FASTEST:
				int proximityScore = Math.max(0,  30 - daysAway);
				score += proximityScore * 1.5;
				break;
			case WEEKEND_OPTIMAL:
				int weekendCount = DateRangeUtils.countWeekend(candidate.getStartDate(), candidate.getEndDate());
				score += weekendCount * 5.0;
				break;
		}
		return score;
	}

	private double getBaseScore(UserStyle userStyle, CandidateType type) {
		switch (userStyle) {
			case RELAX:
				switch (type) {
					case LONGEST: 
						return 1.0;
					case WEEKEND_OPTIMAL: 
						return 0.9;
					case FASTEST: 
						return 0.7;
				}
				break;
	
			case WORK:
				switch (type) {
					case FASTEST: 
						return 1.0;
					case LONGEST: 
						return 0.8;
					case WEEKEND_OPTIMAL: 
						return 0.6;
				}
				break;
	
			case BALANCE:
				switch (type) {
					case LONGEST: 
						return 0.9;
					case FASTEST: 
						return 0.85;
					case WEEKEND_OPTIMAL: 
						return 0.8;
				}
				break;
		}
		return 0.0;
	}

	// TOP1 선정
	// 점수만으로 결정하기 애매할 때, 어떤 기준으로 최종 결정을 내려야 하는가
	private PeriodCandidate selectPrimary(List<ScoredCandidate> scored, UserStyle userStyle) {
		
		if(scored == null || scored.isEmpty()) {
			return null;
		}
		
		if(scored.size() == 1) {
			return scored.get(0).getCandidate();
		}
		
		//1. score 기준 내림차순 정렬
		ScoredCandidate cand1 = scored.get(0);
		ScoredCandidate cand2 = scored.get(1);
		
		double diff = cand1.getScore() - cand2.getScore();
		
		//2. 점수 차이가 충분하면 top1 확정
		if(Math.abs(diff) >= EPSILON) {
			return cand1.getCandidate();
		}
		
		//3. 점수 차이가 애매하면 userStyle 우선순위로 (index가 작을수록)
		List<CandidateType> prioType = userStyle.getPriority();
		
		int p1 = prioType.indexOf(cand1.getCandidate().getType());
		int p2 = prioType.indexOf(cand2.getCandidate().getType());
			
		return p1 <= p2 ? cand1.getCandidate() : cand2.getCandidate();
		
	}
	
	// 후보 선정
	// TOP1이 아닌, 다른 관점의 합리적 선택지는 무엇인가? (primary와 의미-type이 달라야함)
	/**
	 * selectAlternatives 규칙
	 * 1. primary 제외
	 * 2. candidateType별 최고 score 후보 추출
	 * 3. score >= primaryScore * Threshold 인 것만 유지 (Threshold = 0.4 ~ 0.6)
	 * 4. 상위 N개 반환(2개)*/
	private List<PeriodCandidate> selectAlternatives(List<ScoredCandidate> scored, PeriodCandidate primary) {
		List<PeriodCandidate> result = new ArrayList<>();
		
		if(scored == null || scored.isEmpty()) {
			return result;
		}
		
		//EPSILON 비교로 인해 get(0)이 primary가 아닐 수 있으므로 한번 필터링
		double primaryScore = scored.stream()
			    .filter(sc -> sc.getCandidate().equals(primary))
			    .findFirst()
			    .map(ScoredCandidate::getScore)
			    .orElse(0.0);
		double thresholdScore = primaryScore * THRESHOLD; 
		
		//1. candidateType별 최고 score 후보 추출
		Map<CandidateType, ScoredCandidate> bestByType = new HashMap<>();
		
		
		for(ScoredCandidate sc : scored) {
			CandidateType type = sc.getCandidate().getType();
			
			if(type != primary.getType() && !bestByType.containsKey(type)) {
				bestByType.put(type, sc);
			}
		}
		
		//2. threshold 통과만 유지 (type 수 증가에 대한 확장성을 위해 분리)
		List<ScoredCandidate> filtered = new ArrayList<>();
		for(ScoredCandidate sc : bestByType.values()) {
			if(sc.getScore() >= thresholdScore) {
				filtered.add(sc);
			}
		}
		
		//3. score 기준 정렬 후 상위 N개
		filtered.sort(Comparator.comparingDouble(ScoredCandidate::getScore).reversed());
		int limit = Math.min(2, filtered.size());
		for(int i = 0; i < limit; i++) {
			result.add(filtered.get(i).getCandidate());
		}
		
		return result;
	}
	
	// 이유 생성
	// 사용자가 이 추천을 신뢰하게 만들 최소한의 설명은 무엇인가?
	private String generatePrimaryReason(PeriodCandidate primary, UserStyle userStyle) {
		switch (primary.getType()) {
			//LONGEST는 가장 길다는 것을 사용자가 받아들이는 가치가 다르기 때문에 userStyle에 따라 분기
			case LONGEST:
				switch (userStyle) {
					case RELAX:
						return "연속으로 가장 긴 free 기간으로, 휴식에 집중하기 좋은 워케이션 기간입니다.";
					case WORK:
						return "충분히 긴 연속 기간으로, 업무 몰입과 생활 리듬을 유지하기에 적합합니다.";
					case BALANCE:
						return "길이와 여유를 모두 고려했을 때 균형 잡힌 워케이션 기간입니다.";
				}
	
			case FASTEST:
				return "가장 가까운 시작일을 가진 기간으로, 지금 바로 떠나기 좋은 워케이션 일정입니다.";
	
			case WEEKEND_OPTIMAL:
				return "주말 비중이 높아 휴식 효율이 뛰어난 워케이션 기간입니다.";
		}

		return "추천 기준에 부합하는 워케이션 기간입니다.";
	}

}
