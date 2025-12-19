package com.ssafy.chillandcode.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dao.PostDao;
import com.ssafy.chillandcode.model.dto.post.Post;
import com.ssafy.chillandcode.model.dto.post.RegionRank;

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
	public void update(Post post, Long userId) {
		Post original = postDao.selectById(post.getPostId());
		if (original == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}
		if (!original.getUserId().equals(userId)) {
			throw new ApiException(ErrorCode.FORBIDDEN_ACTION, "게시글 작성자만 수정할 수 있습니다.");
		}
		int rows = postDao.update(post);
		if (rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "게시글 수정에 실패했습니다.");
		}
	}

	@Override
	public void delete(Long postId, Long userId) {
		Post original = postDao.selectById(postId);
		if (original == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}
		if (!original.getUserId().equals(userId)) {
			throw new ApiException(ErrorCode.FORBIDDEN_ACTION, "게시글 작성자만 삭제할 수 있습니다.");
		}
		int rows = postDao.delete(postId);
		if (rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "게시글 삭제에 실패했습니다.");
		}
	}

	@Override
	public List<RegionRank> selectRegionRank(Map<String, Object> params) {
		return postDao.selectRegionRank(params);
	}

}
