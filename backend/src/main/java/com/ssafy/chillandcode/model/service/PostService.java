package com.ssafy.chillandcode.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.chillandcode.model.dto.post.Post;
import com.ssafy.chillandcode.model.dto.post.RegionRank;
import com.ssafy.chillandcode.model.dto.post.HashtagRank;

/**
 * PostService는 게시글 정보에 대한 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface PostService {

	/** 게시글 등록
	 */
	void insert(Post post);

	/**
	 * 게시글 목록 조회
	 */
	List<Post> selectAll(Map<String, Object> params);
	
	/**
	 * 내가 쓴 게시글 조회
	 */
	List<Post> findByUserId(Long userId);
	
	/**
	 * 게시글 상세조회
	 */
	Post selectById(Long postId);
	
	/**
	 * 게시글 수정
	 */
	void update(Post post, Long userId);
	
	/**
	 * 게시글 삭제
	 */
	void delete(Long postId, Long userId);

	/**
	 * 지역별 게시글 수 랭킹 조회 (옵션: 기간, 제한 개수)
	 */
	List<RegionRank> selectRegionRank(Map<String, Object> params);

	/**
	 * 게시글 총 개수 조회 (검색 조건 포함)
	 */
	int countAll(Map<String, Object> params);

	/**
	 * 해시태그별 게시글 수 랭킹 조회 (옵션: 기간, 제한 개수)
	 */
	List<HashtagRank> selectHashtagRank(Map<String, Object> params);

}
