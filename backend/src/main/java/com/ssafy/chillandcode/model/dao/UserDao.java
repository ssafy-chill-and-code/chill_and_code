package com.ssafy.chillandcode.model.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.chillandcode.model.dto.user.User;
import com.ssafy.chillandcode.model.dto.user.UserUpdateRequest;

@Mapper
public interface UserDao {
	//회원 가입 (등록)
	int insertUser(User user);
	
	//회원 정보 조회
    User selectUser(Long userId);
	
	//회원 정보 수정
	int updateUser(UserUpdateRequest req);
	
	//회원 정보 삭제(탈퇴)
    int softDelete(Long userId);
	
	//이메일 중복 검사
	int existsByEmail(String email);
	
	//회원 단건 조회 (일반 로그인)
	User findByEmail(String email);
	
	//회원 단건 조회 (OAuth)
	User findByProvider(@Param("provider") String provider,
						@Param("providerId") String providerId);
}
