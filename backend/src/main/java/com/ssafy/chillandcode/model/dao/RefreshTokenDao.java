package com.ssafy.chillandcode.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.chillandcode.model.dto.auth.RefreshToken;

@Mapper
public interface RefreshTokenDao {
	
	//등록
	int insert(RefreshToken token);
	
	//조회
	RefreshToken findByTokenHash(@Param("tokenHash") String tokenHash);
	
	//회수
	int revokeByTokenHash(@Param("tokenHash") String tokenHash);
	
	int revokeAllByUserId(@Param("userId") Long userId);
	
}
