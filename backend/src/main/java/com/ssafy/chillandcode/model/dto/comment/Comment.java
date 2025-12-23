package com.ssafy.chillandcode.model.dto.comment;

import java.time.LocalDateTime;

/**
 * Comment는 댓글 Entity (DB 매핑용)입니다.
 * 조회 결과는 CommentResponse 사용
 */
public class Comment {
	// DB 컬럼만
	private Long commentId;
	private Long postId;
	private Long userId;
	private String content;
	private LocalDateTime createdAt; 

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
