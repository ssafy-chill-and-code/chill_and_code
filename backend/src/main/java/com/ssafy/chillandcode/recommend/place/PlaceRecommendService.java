package com.ssafy.chillandcode.recommend.place;

import java.util.List;
import java.util.Map;

/**
 * 사용자 입력을 받아 장소 추천 카드 결과를 제공하는 서비스
 */
public interface PlaceRecommendService {

    /**
     * 입력값과 LLM 결과를 받아 최종 추천 카드(UI 응답 모델) 리스트를 반환한다
     */
    List<PlaceRecommendCard> recommendCards(
        String style,
        String budget,
        String region,
        String transport
    );
    
}
