package com.ssafy.chillandcode.model.dto.review;

/**
 * 장소 리뷰 수정 요청 DTO
 */
public class PlaceReviewUpdateRequest {
    private Integer rating;
    private String content;

    public PlaceReviewUpdateRequest() {}

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
}

