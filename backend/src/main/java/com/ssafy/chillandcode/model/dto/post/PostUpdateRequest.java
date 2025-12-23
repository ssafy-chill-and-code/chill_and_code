package com.ssafy.chillandcode.model.dto.post;

/**
 * PostUpdateRequest는 게시글 수정 요청 DTO입니다.
 */
public class PostUpdateRequest {
    private String title;
    private String content;
    private String region;
    private String placeUrl;

    public PostUpdateRequest() {}

    public PostUpdateRequest(String title, String content, String region, String placeUrl) {
        this.title = title;
        this.content = content;
        this.region = region;
        this.placeUrl = placeUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPlaceUrl() {
        return placeUrl;
    }

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }
}

