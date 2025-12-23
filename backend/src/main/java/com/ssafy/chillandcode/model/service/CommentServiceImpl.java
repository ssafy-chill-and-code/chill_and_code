package com.ssafy.chillandcode.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dao.CommentDao;
import com.ssafy.chillandcode.model.dao.PostDao;
import com.ssafy.chillandcode.model.dto.comment.Comment;
import com.ssafy.chillandcode.model.dto.comment.CommentResponse;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private PostDao postDao; // 게시글 존재 여부 확인용

	// 댓글 작성
	@Override
	public Long insertComment(Long postId, Long userId, String content) {

		// 게시글이 존재하는지 체크
		if (postDao.selectById(postId) == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}

		Comment comment = new Comment();
		comment.setPostId(postId);
		comment.setUserId(userId);
		comment.setContent(content);

		commentDao.insert(comment);

		return comment.getCommentId();
	}

	// 댓글 목록 조회
	@Override
	public List<CommentResponse> findCommentsByPostId(Long postId) {

		if (postDao.selectById(postId) == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}

		return commentDao.selectByPostId(postId);
	}

	// 내가 쓴 댓글 목록 조회
	@Override
	public List<CommentResponse> findByUserId(Long userId) {
		return commentDao.selectByUserId(userId);
	}

	// 댓글 수정
	@Override
	public void updateComment(Long commentId, Long userId, String content) {

		Comment target = commentDao.selectByCommentId(commentId);

		if (target == null) {
			throw new ApiException(ErrorCode.COMMENT_NOT_FOUND);
		}

		if (!target.getUserId().equals(userId)) {
			throw new ApiException(ErrorCode.FORBIDDEN_ACTION, "댓글 작성자만 수정할 수 있습니다.");
		}

		Comment updated = new Comment();
		updated.setCommentId(commentId);
		updated.setContent(content);

		int rows = commentDao.update(updated);

		if (rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "댓글 수정에 실패했습니다.");
		}

	}

	// 댓글 삭제
	@Override
	public void deleteComment(Long commentId, Long userId) {

		Comment target = commentDao.selectByCommentId(commentId);

		if (target == null) {
			throw new ApiException(ErrorCode.COMMENT_NOT_FOUND);
		}

		if (!target.getUserId().equals(userId)) {
			throw new ApiException(ErrorCode.FORBIDDEN_ACTION, "본인의 댓글만 삭제할 수 있습니다.");
		}

		int rows = commentDao.delete(commentId);
		if (rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
		}

	}

}
