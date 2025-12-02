package com.ssafy.chillandcode.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.ssafy.chillandcode.exception.ApiException;
//import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dao.UserDao;
import com.ssafy.chillandcode.model.dto.user.User;
import com.ssafy.chillandcode.model.dto.user.UserSignUpRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	//회원 가입 (등록)
	@Override
	public void insertUser(UserSignUpRequest req) {
		
		//검증
//		if(!isValidEmail(req.getEmail())) {
//			throw new ApiException(ErrorCode.INVALID_EMAIL_FORMAT);
//		}
//		
//		if(!isValidPassword(req.getPassword())) {
//			throw new ApiException(ErrorCode.INVALID_PASSWORD);
//		}
//		
//		if(!isValidNickname(req.getNickname())) {
//			throw new ApiException(ErrorCode.INVALID_NICKNAME);
//		}
//		
//		//중복 체크
//		if(userDao.existsByEmail(req.getEmail()) > 0) {
//			throw new ApiException(ErrorCode.DUPLICATE_EMAIL);
//		}
		
		
		User user = req.toEntity();
		int rows = userDao.insertUser(user);
//		if(rows != 1) {
//			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "회원가입 처리 중 오류가 발생했습니다.");
//		}
		
		return;
	}
	
	//회원 정보 조회
	@Override
	public User selectUser(long userId) {
		return userDao.selectUser(userId);
	}
	
	//회원 정보 수정
	@Override
	public boolean updateUser(User user) {
		return userDao.updateUser(user) == 1;
	}
	
	//회원 정보 삭제(탈퇴)
	@Override
	public boolean softDelete(long userId) {
		return userDao.softDelete(userId) == 1;
	}
	
	//로그인
	@Override
	public User login(User user) {
		return userDao.login(user);
	}
	
	//검증 로직
	private boolean isValidEmail(String email) {
		if(email == null) return false;
		return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	}
	
	private boolean isValidPassword(String password) {
		if(password == null) return false;
		return password.length() >= 8 && !password.contains(" ");
	}
	
	private boolean isValidNickname(String nickname) {
		if(nickname == null) return false;
		if(nickname.contains(" ")) return false;
		return nickname.length() >= 2 && nickname.length() <= 20;
	}

}
