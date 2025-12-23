package com.ssafy.chillandcode.model.dto.comment;

import java.time.LocalDateTime;

/**
 * Comment는 게시글에 작성된 댓글 정보를 담는 DTO 입니다.
 */
public class Comment {

	private Long commentId; // 댓글 ID 
	private Long postId; // 소속 게시글 ID 
	private Long userId; // 작성자 ID 

	private String content; // 댓글 내용
	private LocalDateTime createdAt; // 작성 시간
	private String postTitle; // 게시글 제목

	// JOIN 결과
	private String nickname; // 작성자 닉네임
	private String profileImageUrl; // 작성자 프로필 이미지 URL 

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
}
