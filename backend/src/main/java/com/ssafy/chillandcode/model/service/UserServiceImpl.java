package com.ssafy.chillandcode.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dao.UserDao;
import com.ssafy.chillandcode.model.dto.user.LoginRequest;
import com.ssafy.chillandcode.model.dto.user.LoginResponse;
import com.ssafy.chillandcode.model.dto.user.User;
import com.ssafy.chillandcode.model.dto.user.UserSignUpRequest;
import com.ssafy.chillandcode.model.dto.user.UserUpdateRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//회원 가입 (등록)
	@Override
	public void insertUser(UserSignUpRequest req) {
		
		//검증
		if(!isValidEmail(req.getEmail())) {
			throw new ApiException(ErrorCode.INVALID_EMAIL_FORMAT);
		}
		
		if(!isValidPassword(req.getPassword())) {
			throw new ApiException(ErrorCode.INVALID_PASSWORD);
		}
		
		if(!isValidNickname(req.getNickname())) {
			throw new ApiException(ErrorCode.INVALID_NICKNAME);
		}
		
		//중복 체크
		if(userDao.existsByEmail(req.getEmail()) > 0) {
			throw new ApiException(ErrorCode.DUPLICATE_EMAIL);
		}
		
		
		User user = req.toEntity();
		
		// BCrypt 적용
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		int rows = userDao.insertUser(user);
		if(rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "회원가입 처리 중 오류가 발생했습니다.");
		}
		
		return;
	}
	
	//회원 정보 조회
	@Override
	public User selectUser(long userId) {
		return userDao.selectUser(userId);
	}
	
	//회원 정보 수정
	@Override
	public boolean updateUser(long userId, UserUpdateRequest req) {
		req.setUserId(userId);
		return userDao.updateUser(req) == 1;
	}
	
	//회원 정보 삭제(탈퇴)
	@Override
	public boolean softDelete(long userId) {
		return userDao.softDelete(userId) == 1;
	}
	
	//로그인
	@Override
	public LoginResponse login(LoginRequest req) {
		
		// 이메일로 사용자 조회 (존재하지 않으면 INVALID_LOGIN 처리)
		User user = userDao.findByEmail(req.getEmail());
		if(user==null) {
			throw new ApiException(ErrorCode.INVALID_LOGIN);
		}
		
		// 보안상 탈퇴 사용자도 별도 처리
		if(user.getIsDeleted() == 1) {
			throw new ApiException(ErrorCode.DELETED_USER);
		}
		
		// 비밀번호 불일치 시 동일 에러 코드로 처리 (사용자 정보 유추 방지)
		boolean ok = passwordEncoder.matches(req.getPassword(), user.getPassword());
		if(!ok) {
			throw new ApiException(ErrorCode.INVALID_LOGIN);
		}
		
		LoginResponse response = LoginResponse.from(user);
		
		return response;
	}
	
	
	
	/**
		검증로직
	 */
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
