package com.ssafy.chillandcode.model.dto.like;

import java.time.LocalDateTime;

/**
 * PostLike는 게시글 좋아요 Entity (DB 매핑용)입니다.
 */
public class PostLike {
    private Long likeId;
    private Long postId;
    private Long userId;
    private LocalDateTime createdAt;

    public PostLike() {}

    public PostLike(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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

