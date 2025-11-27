package com.ssafy.chillandcode.model.service;

import java.util.List;

import com.ssafy.chillandcode.model.dto.Post;

/**
 * PostService는 게시글 정보에 대한 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface PostService {

	/** 게시글 등록
	 */
	int insert(Post post);

	/**
	 * 게시글 목록 조회
	 */
	List<Post> selectAll(String region);
	
	/**
	 * 게시글 상세조회
	 */
	Post selectById(Long postId);
	
	/**
	 * 게시글 수정
	 */
	int update(Post post);
	
	/**
	 * 게시글 삭제
	 */
	int delete(Long postId);
	

}
