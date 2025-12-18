package com.ssafy.chillandcode.recommend.place;

import java.util.List;

/**
 * 사용자 입력을 받아 
 * 장소 추천 전체 흐름을 조율하는 서비스 
 */
public interface PlaceRecommendService {

	/**
	 * 사용자 입력(style, budget, region)을 받아 
	 * Repository와 Engine을 연결해 추천 결과를 생성한다
	 */
	
	List<PlaceFeature> recommend(String style, String budget, String sido);
}
