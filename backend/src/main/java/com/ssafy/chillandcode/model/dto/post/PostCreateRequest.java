package com.ssafy.chillandcode.model.dto.post;

/**
 * PostCreateRequest는 게시글 작성 요청 DTO입니다.
 */
public class PostCreateRequest {
    private String title;
    private String content;
    private String region;

    public PostCreateRequest() {}

    public PostCreateRequest(String title, String content, String region) {
        this.title = title;
        this.content = content;
        this.region = region;
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
}

