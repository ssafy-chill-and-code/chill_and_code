package com.ssafy.chillandcode.model.service;

import java.util.List;

import com.ssafy.chillandcode.model.dto.Post;

/**
 * PostService??寃뚯떆湲 ?뺣낫?????鍮꾩쫰?덉뒪 濡쒖쭅??泥섎━?섎뒗 ?쒕퉬???명꽣?섏씠?ㅼ엯?덈떎.
 */
public interface PostService {

	/** 寃뚯떆湲 ?깅줉
	 */
	int insert(Post post);

	/**
	 * 寃뚯떆湲 紐⑸줉 議고쉶
	 */
	List<Post> selectAll(String region);
	
	/**
	 * 寃뚯떆湲 ?곸꽭議고쉶
	 */
	Post selectById(Long postId);
	
	/**
	 * 寃뚯떆湲 ?섏젙
	 */
	int update(Post post);
	
	/**
	 * 寃뚯떆湲 ??젣
	 */
	int delete(Long postId);
	

}
