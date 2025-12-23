package com.ssafy.chillandcode.model.dto.review;

import java.util.List;

/**
 * 장소 리뷰 요약 정보 응답 DTO
 */
public class PlaceReviewSummaryResponse {
    private String placeName;
    private String region;
    private Double averageRating;
    private Integer reviewCount;
    private List<PlaceReviewResponse> reviews;

    public PlaceReviewSummaryResponse() {}

    public PlaceReviewSummaryResponse(String placeName, String region, Double averageRating, Integer reviewCount, List<PlaceReviewResponse> reviews) {
        this.placeName = placeName;
        this.region = region;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.reviews = reviews;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<PlaceReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<PlaceReviewResponse> reviews) {
        this.reviews = reviews;
    }
}

