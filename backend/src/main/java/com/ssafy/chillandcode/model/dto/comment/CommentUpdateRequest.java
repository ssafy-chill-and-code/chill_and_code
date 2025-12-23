package com.ssafy.chillandcode.model.dto.comment;

/**
 * CommentUpdateRequest는 댓글 수정 요청 DTO입니다.
 */
public class CommentUpdateRequest {
    private String content;

    public CommentUpdateRequest() {}

    public CommentUpdateRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
