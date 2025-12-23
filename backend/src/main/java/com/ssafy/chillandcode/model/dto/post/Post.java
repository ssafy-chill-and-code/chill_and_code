package com.ssafy.chillandcode.model.dto.post;

import java.time.LocalDateTime;

/**
 * Post는 커뮤니티 게시글 정보를 담는 DTO 입니다.
 */
public class Post {
    private Long postId;    // 게시글 ID
    private Long userId;    // 작성자 ID
    private String title;   // 제목
    private String content; // 내용
    private String region;  // 지역별 게시판 필터링
    private LocalDateTime createdAt; // 작성일
    private String nickname; // 작성자 닉네임
    private Integer viewCount; // 조회수
    private Integer commentCount; // 댓글 개수
    private String profileImageUrl; // 작성자 프로필 이미지 URL

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

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}
