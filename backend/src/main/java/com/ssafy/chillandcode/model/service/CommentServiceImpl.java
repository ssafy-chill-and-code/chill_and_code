package com.ssafy.chillandcode.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.CommentDao;
import com.ssafy.chillandcode.model.dao.PostDao;
import com.ssafy.chillandcode.model.dto.Comment;
import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;

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
    public List<Comment> findCommentsByPostId(Long postId) {

        if (postDao.selectById(postId) == null) {
            throw new ApiException(ErrorCode.POST_NOT_FOUND);
        }

        return commentDao.selectByPostId(postId);
    }

    // 내가 쓴 댓글 목록 조회
    @Override
    public List<Comment> findByUserId(Long userId) {
    	return commentDao.selectByPostId(userId);
    }
    
    // 댓글 수정
    @Override
    public boolean updateComment(Long commentId, Long userId, String content) {

        Comment target = commentDao.selectByCommentId(commentId);

        if (target == null) throw new ApiException(ErrorCode.COMMENT_NOT_FOUND);
        if (!target.getUserId().equals(userId)) throw new ApiException(ErrorCode.COMMENT_OWNER_MISMATCH);

        Comment updated = new Comment();
        updated.setCommentId(commentId);
        updated.setContent(content);

        if (commentDao.update(updated) <= 0) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 댓글 삭제
    @Override
    public boolean deleteComment(Long commentId, Long userId) {

        Comment target = commentDao.selectByCommentId(commentId);

        if (target == null) throw new ApiException(ErrorCode.COMMENT_NOT_FOUND);
        if (!target.getUserId().equals(userId)) throw new ApiException(ErrorCode.COMMENT_OWNER_MISMATCH);

        if (commentDao.delete(commentId) <= 0) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

}
