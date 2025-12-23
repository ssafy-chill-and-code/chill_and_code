package com.ssafy.chillandcode.model.service.review;

import com.ssafy.chillandcode.model.dto.review.PlaceReviewSummaryResponse;

public interface ReviewService {

    /**
     * 장소명과 지역으로 리뷰 요약 정보 조회
     */
    PlaceReviewSummaryResponse getPlaceReviewSummary(String placeName, String region);
}

