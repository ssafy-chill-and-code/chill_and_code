package com.ssafy.chillandcode.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dao.LikeDao;
import com.ssafy.chillandcode.model.dao.PostDao;
import com.ssafy.chillandcode.model.dto.like.PostLike;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeDao likeDao;
    
    @Autowired
    private PostDao postDao;

    @Override
    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        // 게시글 존재 여부 확인
        if (postDao.selectById(postId) == null) {
            throw new ApiException(ErrorCode.POST_NOT_FOUND);
        }
        
        // 이미 좋아요를 눌렀는지 확인
        PostLike existingLike = likeDao.selectByPostIdAndUserId(postId, userId);
        
        if (existingLike != null) {
            // 좋아요 취소
            likeDao.delete(postId, userId);
            return false;
        } else {
            // 좋아요 추가
            PostLike like = new PostLike(postId, userId);
            likeDao.insert(like);
            return true;
        }
    }

    @Override
    public boolean isLiked(Long postId, Long userId) {
        return likeDao.selectByPostIdAndUserId(postId, userId) != null;
    }

    @Override
    public int getLikeCount(Long postId) {
        return likeDao.countByPostId(postId);
    }
}

