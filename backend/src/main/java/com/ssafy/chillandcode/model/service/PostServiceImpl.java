package com.ssafy.chillandcode.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.dao.PostDao;
import com.ssafy.chillandcode.model.dto.Post;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostDao postDao;

	@Override
	public int insert(Post post) {
		return postDao.insert(post);
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
		return postDao.update(post);
	}

	@Override
	public int delete(Long postId) {
		return postDao.delete(postId);
	}

}
