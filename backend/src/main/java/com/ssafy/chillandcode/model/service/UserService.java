package com.ssafy.chillandcode.model.service;

import com.ssafy.chillandcode.model.dto.user.User;
import com.ssafy.chillandcode.model.dto.UserSignUpRequest;

public interface UserService {
	// 회원 가입 (등록)
	void insertUser(UserSignUpRequest req);

	// 회원 정보 조회
	User selectUser(long userId);

	// 회원 정보 수정
	boolean updateUser(User user);

	// 회원 정보 삭제(탈퇴)
	boolean softDelete(long userId);

	// 로그인
	User login(User user);
}
