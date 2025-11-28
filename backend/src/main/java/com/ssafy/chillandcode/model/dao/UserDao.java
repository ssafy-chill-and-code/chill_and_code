package com.ssafy.chillandcode.model.dao;


import org.apache.ibatis.annotations.Mapper;

import com.ssafy.chillandcode.model.dto.User;

@Mapper
public interface UserDao {
	//회원 가입 (등록)
	int insertUser(User user);
	
	//회원 정보 조회
	User selectUser(long userId);
	
	//회원 정보 수정
	int updateUser(User user);
	
	//회원 정보 삭제(탈퇴)
	int softDelete(long userId);
	
	//로그인
	User login(User user);
}
