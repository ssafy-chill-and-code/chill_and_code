package com.ssafy.chillandcode.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dao.UserDao;
import com.ssafy.chillandcode.model.dto.user.LoginRequest;
import com.ssafy.chillandcode.model.dto.user.LoginResponse;
import com.ssafy.chillandcode.model.dto.user.PasswordChangeRequest;
import com.ssafy.chillandcode.model.dto.user.User;
import com.ssafy.chillandcode.model.dto.user.UserSignUpRequest;
import com.ssafy.chillandcode.model.dto.user.UserUpdateRequest;
import com.ssafy.chillandcode.security.jwt.JwtTokenProvider;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
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
		
	}
	
	//회원 정보 조회
	@Override
    public User selectUser(Long userId) {
		
		User user = userDao.selectUser(userId);
		if(user == null) {
			throw new ApiException(ErrorCode.USER_NOT_FOUND);
		}
		
		return user;
	}
	
	
	//회원 정보 수정
	@Override
    public void updateUser(Long userId, UserUpdateRequest req) {
		
		// 수정 요청에 nickname이 포함되어 있다면 반드시 검증
		if (req.getNickname() != null) {
		    if (!isValidNickname(req.getNickname())) {
		        throw new ApiException(ErrorCode.INVALID_NICKNAME);
		    }
		}
		
		req.setUserId(userId);
		
		int rows = userDao.updateUser(req);
		if(rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "프로필 수정에 실패했습니다.");
		}
	}
	
	
	//비밀번호 변경
	@Override
	public void changePassword(Long userId, PasswordChangeRequest req) {
		
		//사용자 조회
		User user = userDao.selectUser(userId);
		if(user == null) {
			throw new ApiException(ErrorCode.USER_NOT_FOUND);
		}
		
		//OAuth 사용자 차단 (OAuth 사용자는 외부 인증을 사용하므로 내부 비밀번호 변경 기능 제공X)
		if(user.getProvider() != null) {
			throw new ApiException(ErrorCode.OAUTH_USER_PASSWORD_NOT_ALLOWED);
		}
		
		//기존 비밀번호 검증
		if(!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
			throw new ApiException(ErrorCode.INVALID_CURRENT_PASSWORD);
		}
		
		//새 비밀번호 형식 검증
		if(!isValidPassword(req.getNewPassword())) {
			throw new ApiException(ErrorCode.INVALID_NEW_PASSWORD);
		}
		
		//새비밀번호 암호화
		String encoded = passwordEncoder.encode(req.getNewPassword());
		
		//DB 업데이트
		int rows = userDao.updatePassword(userId, encoded);
		if(rows != 1) {
			throw new ApiException(ErrorCode.PASSWORD_UPDATE_FAILED);
		}
		
		//Refresh Token 무효화
		refreshTokenService.revokeAllByUserId(userId);
	}
	
	//회원 정보 삭제(탈퇴)
	@Override
    public void softDelete(Long userId) {
		int rows = userDao.softDelete(userId);
		if(rows != 1) {
			throw new ApiException(ErrorCode.USER_NOT_FOUND);
		}
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
		
		// BCrypt 검증 - 비밀번호 불일치 시 동일 에러 코드로 처리 (사용자 정보 유추 방지)
		boolean ok = passwordEncoder.matches(req.getPassword(), user.getPassword());
		if(!ok) {
			throw new ApiException(ErrorCode.INVALID_LOGIN);
		}
		
		// access token 발급
		String accessToken = jwtTokenProvider.createAccessToken(user.getUserId());
		LoginResponse response = LoginResponse.from(user, accessToken);
		
		return response;
	}
	
	// OAuth 사용자 조회 또는 생성
	@Override
	public User findOrCreateOAuthUser(OAuth2User oAuth2User) {
		
		System.out.println("🔥 OAuth UserService 진입");

		
		// 정보 추출
		Map<String, Object> attributes = oAuth2User.getAttributes();
		
		String email = (String) attributes.get("email");
		String provider = "google"; // 현재는 google 고정
		String providerId = (String) attributes.get("sub");
		
		System.out.println("provider=google, providerId=" + providerId);
		
		// 기존 OAuth 사용자 조회
		User existingOAuthUser = userDao.findByProvider(provider, providerId);
		if(existingOAuthUser != null) {
			return existingOAuthUser;
		}
		
		// 이메일 중복 (일반 회원과 충돌 방지)
		User existingEmailUser = userDao.findByEmail(email);
		if(existingEmailUser != null) {
			throw new ApiException(ErrorCode.OAUTH_EMAIL_DUPLICATED, "OAuth 가입 실패: 이미 사용 중인 이메일입니다.");
		}
		
		User newUser = new User();
		newUser.setEmail(email);
		newUser.setProvider(provider);
		newUser.setProviderId(providerId);
		newUser.setPassword(null);
		
		// 필수 필드 기본값 설정
		newUser.setNickname("google_" + providerId.substring(0, 6));
		newUser.setRegion(null);
		
		// 저장
		int rows = userDao.insertUser(newUser);
		
		if(rows != 1) {
			throw new ApiException(ErrorCode.OAUTH_USER_CREATE_FAILED, "OAuth 사용자 생성 중 오류가 발생했습니다.");
		}
		
		return newUser;
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
