package com.ssafy.chillandcode.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.CommentDao;
import com.ssafy.chillandcode.model.dao.PostDao;
import com.ssafy.chillandcode.model.dto.comment.Comment;

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
            return null; // Controller에서 404 처리
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
            return null; // Controller에서 404 처리
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

        if (target == null) return false;                     // 댓글 없음
        if (!target.getUserId().equals(userId)) return false; // 작성자가 아니면 수정 불가

        Comment updated = new Comment();
        updated.setCommentId(commentId);
        updated.setContent(content);

        return commentDao.update(updated) > 0;
    }

    // 댓글 삭제
    @Override
    public boolean deleteComment(Long commentId, Long userId) {

        Comment target = commentDao.selectByCommentId(commentId);

        if (target == null) return false;                     // 댓글 없음
        if (!target.getUserId().equals(userId)) return false; // 작성자가 아니면 삭제 불가

        return commentDao.delete(commentId) > 0;
    }

}
