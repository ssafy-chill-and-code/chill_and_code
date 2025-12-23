package com.ssafy.chillandcode.model.dto.comment;

/**
 * CommentCreateRequest는 댓글 작성 요청 DTO입니다.
 */
public class CommentCreateRequest {
    private String content;

    public CommentCreateRequest() {}

    public CommentCreateRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
