package com.ssafy.chillandcode.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.chillandcode.model.dto.comment.Comment;
import com.ssafy.chillandcode.model.dto.comment.CommentResponse;

/**
 * CommentDao는 댓글 정보에 대한 DB 접근을 담당하는 MyBatis 매퍼 인터페이스입니다.
 */
@Mapper
public interface CommentDao {

	/** 댓글 등록 */
	int insert(Comment comment);

	/** 댓글 목록 조회 */
	List<CommentResponse> selectByPostId(Long postId);

	/** 내가 쓴 댓글 목록 조회 */
	List<CommentResponse> selectByUserId(Long userId);

	/** 댓글 상세 조회 */
	Comment selectByCommentId(Long commentId);

	/** 댓글 수정 */
	int update(Comment comment);

	/** 댓글 삭제 */
	int delete(Long commentId);
}
