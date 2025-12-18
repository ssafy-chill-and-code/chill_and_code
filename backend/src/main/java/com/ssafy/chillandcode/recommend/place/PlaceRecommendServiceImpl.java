package com.ssafy.chillandcode.recommend.place;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 장소 추천 서비스 구현체 - 사용자 입력을 받아 추천 흐름을 실행한다
 */
@Service
public class PlaceRecommendServiceImpl implements PlaceRecommendService {

	private final PlaceRepository placeRepository;

	public PlaceRecommendServiceImpl(PlaceRepository placeRepository) {
		this.placeRepository = placeRepository;
	}

	@Override
	public List<PlaceFeature> recommend(String style, String budget, String sido) {
		// budget -> priceLevel 범위로 변환
		int minPrice;
		int maxPrice;

		if ("LOW".equals(budget)) {
			minPrice = 1;
			maxPrice = 2;
		} else if ("MID".equals(budget)) {
			minPrice = 2;
			maxPrice = 3;
		} else {
			minPrice = 3;
			maxPrice = 5;
		}

		// 후보 조회 ( region + budget 필터 )
		List<PlaceFeature> candidates = placeRepository.findByRegionAndBudget(sido, minPrice, maxPrice);

		// style -> 가중치선택
		WeightStrategy weight = StyleWeightStrategy.byStyle(style);

		// 점수계산 + 정렬
		candidates.sort(
			    Comparator.comparingDouble(
			        (PlaceFeature p) -> PlaceScoreEngine.calculate(p, weight)
			    ).reversed()
			);

		return candidates;
	}

}
