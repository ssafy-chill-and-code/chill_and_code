package com.ssafy.chillandcode.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.chillandcode.model.dto.Post;

/**
 * PostDao는 게시글 정보에 대한 DB접근을 담당하는 MyBatis 매퍼 인터페이스입니다.
 */

@Mapper
public interface PostDao {

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
