package com.ssafy.chillandcode.model.service;

import com.ssafy.chillandcode.model.dto.user.LoginRequest;
import com.ssafy.chillandcode.model.dto.user.LoginResponse;
import com.ssafy.chillandcode.model.dto.user.User;
import com.ssafy.chillandcode.model.dto.user.UserSignUpRequest;
import com.ssafy.chillandcode.model.dto.user.UserUpdateRequest;

public interface UserService {
	// 회원 가입 (등록)
	void insertUser(UserSignUpRequest req);

	// 회원 정보 조회
    User selectUser(Long userId);

	// 회원 정보 수정
    void updateUser(Long userId, UserUpdateRequest req);

	// 회원 정보 삭제(탈퇴)
    void softDelete(Long userId);

	// 로그인
	LoginResponse login(LoginRequest req);
}
