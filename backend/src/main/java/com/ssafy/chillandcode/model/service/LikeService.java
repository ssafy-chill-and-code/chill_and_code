package com.ssafy.chillandcode.model.service;

/**
 * LikeService는 게시글 좋아요 비즈니스 로직을 담당합니다.
 */
public interface LikeService {
    
    /**
     * 좋아요 토글 (좋아요 추가 또는 취소)
     * @return 좋아요 상태 (true: 추가됨, false: 취소됨)
     */
    boolean toggleLike(Long postId, Long userId);
    
    /**
     * 좋아요 여부 확인
     */
    boolean isLiked(Long postId, Long userId);
    
    /**
     * 게시글 좋아요 개수 조회
     */
    int getLikeCount(Long postId);
}

