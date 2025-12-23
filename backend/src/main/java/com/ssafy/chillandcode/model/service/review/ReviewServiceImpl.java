package com.ssafy.chillandcode.model.service.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.review.ReviewDao;
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
}

