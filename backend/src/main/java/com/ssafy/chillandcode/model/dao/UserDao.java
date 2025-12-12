package com.ssafy.chillandcode.model.dao;


import org.apache.ibatis.annotations.Mapper;

import com.ssafy.chillandcode.model.dto.user.LoginRequest;
import com.ssafy.chillandcode.model.dto.user.LoginResponse;
import com.ssafy.chillandcode.model.dto.user.User;
import com.ssafy.chillandcode.model.dto.user.UserUpdateRequest;

@Mapper
public interface UserDao {
	//회원 가입 (등록)
	int insertUser(User user);
	
	//회원 정보 조회
	User selectUser(long userId);
	
	//회원 정보 수정
	int updateUser(UserUpdateRequest req);
	
	//회원 정보 삭제(탈퇴)
	int softDelete(long userId);
	
	//이메일 중복 검사
	int existsByEmail(String email);
	
	//회원 1명 조회
	User findByEmail(String email);
}
