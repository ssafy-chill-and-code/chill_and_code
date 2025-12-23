package com.ssafy.chillandcode.model.dao;

import org.apache.ibatis.annotations.Mapper;
import com.ssafy.chillandcode.model.dto.like.PostLike;

/**
 * LikeDao는 게시글 좋아요 관련 데이터베이스 접근을 담당합니다.
 */
@Mapper
public interface LikeDao {
    
    /**
     * 좋아요 추가
     */
    int insert(PostLike like);
    
    /**
     * 좋아요 삭제
     */
    int delete(Long postId, Long userId);
    
    /**
     * 특정 사용자가 특정 게시글에 좋아요를 눌렀는지 확인
     */
    PostLike selectByPostIdAndUserId(Long postId, Long userId);
    
    /**
     * 특정 게시글의 좋아요 개수 조회
     */
    int countByPostId(Long postId);
}

