package com.ssafy.chillandcode.controller.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.model.dto.review.PlaceReviewSummaryResponse;
import com.ssafy.chillandcode.model.service.review.ReviewService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews")
    @Operation(summary = "장소 리뷰 조회", description = "장소명과 지역으로 리뷰 목록과 평균 별점을 조회합니다.")
    public ResponseEntity<?> getPlaceReviews(
            @RequestParam(required = true) String placeName,
            @RequestParam(required = false) String region) {

        PlaceReviewSummaryResponse summary = reviewService.getPlaceReviewSummary(placeName.trim(), region);

        return ResponseEntity.ok(ApiResponse.success(summary));
    }
}

