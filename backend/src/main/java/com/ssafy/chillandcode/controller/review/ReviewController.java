package com.ssafy.chillandcode.controller.review;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.model.dto.review.PlaceReviewCreateRequest;
import com.ssafy.chillandcode.model.dto.review.PlaceReviewUpdateRequest;
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

    @PostMapping("/reviews")
    @Operation(summary = "장소 리뷰 작성", description = "장소에 대한 리뷰(별점 + 내용)를 작성합니다.")
    public ResponseEntity<?> createReview(
            @AuthenticationPrincipal Long userId,
            @RequestBody PlaceReviewCreateRequest request) {

        Long reviewId = reviewService.insertReview(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("리뷰가 성공적으로 등록되었습니다.", Map.of("reviewId", reviewId)));
    }

    @PatchMapping("/reviews/{reviewId}")
    @Operation(summary = "장소 리뷰 수정", description = "본인이 작성한 리뷰를 수정합니다.")
    public ResponseEntity<?> updateReview(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long reviewId,
            @RequestBody PlaceReviewUpdateRequest request) {

        reviewService.updateReview(reviewId, request, userId);

        return ResponseEntity.ok(ApiResponse.success("리뷰가 성공적으로 수정되었습니다."));
    }

    @DeleteMapping("/reviews/{reviewId}")
    @Operation(summary = "장소 리뷰 삭제", description = "본인이 작성한 리뷰를 삭제합니다.")
    public ResponseEntity<?> deleteReview(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId, userId);

        return ResponseEntity.ok(ApiResponse.success("리뷰가 성공적으로 삭제되었습니다."));
    }
}

