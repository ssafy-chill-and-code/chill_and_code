package com.ssafy.chillandcode.model.service.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dao.review.ReviewDao;
import com.ssafy.chillandcode.model.dto.review.PlaceReview;
import com.ssafy.chillandcode.model.dto.review.PlaceReviewCreateRequest;
import com.ssafy.chillandcode.model.dto.review.PlaceReviewResponse;
import com.ssafy.chillandcode.model.dto.review.PlaceReviewSummaryResponse;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDao reviewDao;

    @Override
    public PlaceReviewSummaryResponse getPlaceReviewSummary(String placeName, String region) {
        Map<String, Object> params = new HashMap<>();
        params.put("placeName", placeName);
        params.put("region", region);

        // 리뷰 목록 조회
        List<PlaceReviewResponse> reviews = reviewDao.selectByPlaceName(params);

        // 리뷰 개수 조회
        int reviewCount = reviewDao.countByPlaceName(params);

        // 평균 별점 조회
        Double averageRating = reviewDao.selectAverageRating(params);

        // 평균 별점이 null이면 0.0으로 설정
        if (averageRating == null) {
            averageRating = 0.0;
        }

        // 소수점 둘째 자리까지 반올림
        averageRating = Math.round(averageRating * 100.0) / 100.0;

        return new PlaceReviewSummaryResponse(
            placeName,
            region,
            averageRating,
            reviewCount,
            reviews
        );
    }

    @Override
    public Long insertReview(PlaceReviewCreateRequest request, Long userId) {
        // placeName 필수 검증
        if (request.getPlaceName() == null || request.getPlaceName().trim().isEmpty()) {
            throw new ApiException(ErrorCode.MISSING_REQUIRED_PARAMETER, "장소명은 필수입니다.");
        }

        // rating 범위 검증 (1~5)
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new ApiException(ErrorCode.INVALID_RATING);
        }

        // Entity 생성
        PlaceReview review = new PlaceReview();
        review.setPlaceName(request.getPlaceName().trim());
        review.setRegion(request.getRegion());
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setUserId(userId);

        // DB 저장
        int rows = reviewDao.insert(review);

        if (rows != 1) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "리뷰 작성에 실패했습니다.");
        }

        return review.getId();
    }
}

