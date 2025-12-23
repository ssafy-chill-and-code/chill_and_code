package com.ssafy.chillandcode.model.dto.review;

/**
 * 장소 리뷰 작성 요청 DTO
 */
public class PlaceReviewCreateRequest {
    private String placeName;
    private String region;
    private Integer rating;
    private String content;

    public PlaceReviewCreateRequest() {}

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
}

