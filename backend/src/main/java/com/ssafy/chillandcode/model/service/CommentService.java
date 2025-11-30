package com.ssafy.chillandcode.model.service;

import java.util.List;

import com.ssafy.chillandcode.model.dto.Comment;

/**
 * CommentService는 댓글 등록/조회/수정/삭제 비즈니스 로직을 담당합니다.
 */
public interface CommentService {

	/**
	 * 댓글 등록
	 */
	Long insertComment(Long postId, Long userId, String content);

	/**
	 * 댓글 목록 조회
	 */
	List<Comment> findCommentsByPostId(Long postId);

	/**
	 * 내가 쓴 댓글 목록 조회
	 */
	List<Comment> findByUserId(Long userId);
	
	/**
	 * 댓글 수정
	 */
	boolean updateComment(Long commentId, Long userId, String content);

	/**
	 * 댓글 삭제
	 */
	boolean deleteComment(Long commentId, Long userId);


}
