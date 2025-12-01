package com.ssafy.chillandcode.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.PostDao;
import com.ssafy.chillandcode.model.dto.Post;
import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostDao postDao;

	@Override
    public int insert(Post post) {
		int rows = postDao.insert(post);
		if (rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		return rows;
    }

	@Override
	public List<Post> selectAll(Map<String, Object> params) {
		return postDao.selectAll(params);
	}

	@Override
	public List<Post> findByUserId(Long userId) {
		return postDao.selectByUserId(userId);
	}

	@Override
	public Post selectById(Long postId) {
		return postDao.selectById(postId);
	}

	@Override
    public int update(Post post) {
		int rows = postDao.update(post);
		if (rows != 1) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}
		return rows;
    }

	@Override
    public int delete(Long postId) {
		int rows = postDao.delete(postId);
		if (rows != 1) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}
		return rows;
    }

}
