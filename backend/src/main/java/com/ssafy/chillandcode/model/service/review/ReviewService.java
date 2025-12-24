package com.ssafy.chillandcode.model.service.review;

import com.ssafy.chillandcode.model.dto.review.PlaceReviewCreateRequest;
import com.ssafy.chillandcode.model.dto.review.PlaceReviewUpdateRequest;
import com.ssafy.chillandcode.model.dto.review.PlaceReviewSummaryResponse;

public interface ReviewService {

    /**
     * 장소명과 지역으로 리뷰 요약 정보 조회
     */
    PlaceReviewSummaryResponse getPlaceReviewSummary(String placeName, String region);

    /**
     * 장소 리뷰 작성
     */
    Long insertReview(PlaceReviewCreateRequest request, Long userId);

    /**
     * 장소 리뷰 수정
     */
    void updateReview(Long reviewId, PlaceReviewUpdateRequest request, Long userId);

    /**
     * 장소 리뷰 삭제
     */
    void deleteReview(Long reviewId, Long userId);
}

