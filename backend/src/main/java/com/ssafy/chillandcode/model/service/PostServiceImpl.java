package com.ssafy.chillandcode.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dao.PostDao;
import com.ssafy.chillandcode.model.dto.post.Post;
import com.ssafy.chillandcode.model.dto.post.PostCreateRequest;
import com.ssafy.chillandcode.model.dto.post.PostUpdateRequest;
import com.ssafy.chillandcode.model.dto.post.PostSummaryResponse;
import com.ssafy.chillandcode.model.dto.post.PostDetailResponse;
import com.ssafy.chillandcode.model.dto.post.RegionRank;
import com.ssafy.chillandcode.model.dto.post.HashtagRank;
import com.ssafy.chillandcode.util.HashtagExtractor;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostDao postDao;

	// 게시글 등록
	@Override
	public Long insert(PostCreateRequest request, Long userId) {
		// Request -> Entity 변환
		Post post = new Post();
		post.setUserId(userId);
		post.setTitle(request.getTitle());
		post.setContent(request.getContent());
		post.setRegion(request.getRegion());
		post.setPlaceUrl(request.getPlaceUrl());
		
		// 해시태그 자동 추출 및 저장
		String tags = HashtagExtractor.extractAndSerialize(
			request.getTitle(), 
			request.getContent(), 
			request.getRegion()
		);
		post.setTags(tags);

		int rows = postDao.insert(post);

		if (rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "게시글 등록에 실패했습니다.");
		}
		
		return post.getPostId();
	}

	// 게시글 목록 조회
	@Override
	public List<PostSummaryResponse> selectAll(Map<String, Object> params) {
		return postDao.selectAll(params);
	}

	// 내가 쓴 게시글 조회
	@Override
	public List<PostSummaryResponse> findByUserId(Long userId) {
		return postDao.selectByUserId(userId);
	}

	// 게시글 상세조회
	@Override
	public PostDetailResponse selectById(Long postId) {
		PostDetailResponse post = postDao.selectById(postId);
		if (post == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}

		// 조회수 증가
		postDao.incrementViewCount(postId);

		return post;
	}

	// 게시글 수정
	@Override
	public void update(Long postId, PostUpdateRequest request, Long userId) {
		PostDetailResponse original = postDao.selectById(postId);
		if (original == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}
		if (!original.getUserId().equals(userId)) {
			throw new ApiException(ErrorCode.FORBIDDEN_ACTION, "게시글 작성자만 수정할 수 있습니다.");
		}
		
		// Request -> Entity 변환
		Post post = new Post();
		post.setPostId(postId);
		post.setTitle(request.getTitle());
		post.setContent(request.getContent());
		post.setRegion(request.getRegion());
		post.setPlaceUrl(request.getPlaceUrl());
		
		// 해시태그 자동 추출 및 저장
		String title = request.getTitle() != null ? request.getTitle() : original.getTitle();
		String content = request.getContent() != null ? request.getContent() : original.getContent();
		String region = request.getRegion() != null ? request.getRegion() : original.getRegion();
		
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
