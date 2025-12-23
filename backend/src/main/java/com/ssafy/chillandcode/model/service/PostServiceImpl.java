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
import com.ssafy.chillandcode.model.dto.post.HashtagRank;
import com.ssafy.chillandcode.util.HashtagExtractor;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostDao postDao;

	// 게시글 등록
	@Override
	public void insert(Post post) {
		// 해시태그 자동 추출 및 저장
		String tags = HashtagExtractor.extractAndSerialize(
			post.getTitle(), 
			post.getContent(), 
			post.getRegion()
		);
		post.setTags(tags);

		int rows = postDao.insert(post);

		if (rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "게시글 등록에 실패했습니다.");
		}
	}

	// 게시글 목록 조회
	@Override
	public List<Post> selectAll(Map<String, Object> params) {
		return postDao.selectAll(params);
	}

	// 내가 쓴 게시글 조회
	@Override
	public List<Post> findByUserId(Long userId) {
		return postDao.selectByUserId(userId);
	}

	// 게시글 상세조회
	@Override
	public Post selectById(Long postId) {
		Post post = postDao.selectById(postId);
		if (post == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}

		// 조회수 증가
		postDao.incrementViewCount(postId);

		return post;
	}

	// 게시글 수정
	@Override
	public void update(Post post, Long userId) {
		Post original = postDao.selectById(post.getPostId());
		if (original == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}
		if (!original.getUserId().equals(userId)) {
			throw new ApiException(ErrorCode.FORBIDDEN_ACTION, "게시글 작성자만 수정할 수 있습니다.");
		}
		
		// 해시태그 자동 추출 및 저장
		String title = post.getTitle() != null ? post.getTitle() : original.getTitle();
		String content = post.getContent() != null ? post.getContent() : original.getContent();
		String region = post.getRegion() != null ? post.getRegion() : original.getRegion();
		
		String tags = HashtagExtractor.extractAndSerialize(title, content, region);
		post.setTags(tags);
		
		int rows = postDao.update(post);
		if (rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "게시글 수정에 실패했습니다.");
		}
	}

	// 게시글 삭제
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

	// 지역별 게시글 수 랭킹 조회 (옵션: 기간, 제한 개수)
	@Override
	public List<RegionRank> selectRegionRank(Map<String, Object> params) {
		return postDao.selectRegionRank(params);
	}

	// 게시글 총 개수 조회 (검색 조건 포함)
	@Override
	public int countAll(Map<String, Object> params) {
		return postDao.countAll(params);
	}

	// 해시태그별 게시글 수 랭킹 조회 (옵션: 기간, 제한 개수)
	@Override
	public List<HashtagRank> selectHashtagRank(Map<String, Object> params) {
		return postDao.selectHashtagRank(params);
	}

}
