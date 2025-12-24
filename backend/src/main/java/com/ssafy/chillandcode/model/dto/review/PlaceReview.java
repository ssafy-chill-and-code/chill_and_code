package com.ssafy.chillandcode.model.dto.review;

import java.time.LocalDateTime;

/**
 * 장소 리뷰 Entity (DB 매핑용)
 * 조회 결과는 PlaceReviewResponse 사용
 */
public class PlaceReview {
    private Long id;
    private String placeName;
    private String region;
    private Integer rating;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;

    public PlaceReview() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

