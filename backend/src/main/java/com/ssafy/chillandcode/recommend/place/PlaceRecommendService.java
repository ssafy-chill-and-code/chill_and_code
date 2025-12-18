package com.ssafy.chillandcode.recommend.place;

import java.util.List;

/**
 * 사용자 입력을 받아 장소 추천 카드 결과를 제공하는 서비스
 */
public interface PlaceRecommendService {

    /**
     * 입력값을 받아 추천 카드(UI 응답 모델) 리스트를 반환한다
     */
    List<PlaceRecommendCard> recommendCards(
        String style,
        String budget,
        String region,
        String transport
    );
}
