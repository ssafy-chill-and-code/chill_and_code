package com.ssafy.chillandcode.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.ssafy.chillandcode.model.service.RefreshTokenService;
import com.ssafy.chillandcode.security.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 로그인 성공 후 토큰을 발급하는 "유일한 진입점"
 *
 * ✔ 일반 로그인 / OAuth 로그인 공통 사용
 * ✔ 쿠키 옵션 통일
 */

@Service
public class AuthTokenService {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	public void issueTokens(Long userId, HttpServletResponse response) {
		
		System.out.println("🔥 AuthTokenService.issueTokens 실행");
		System.out.println("userId = " + userId);

		// Refresh Token 생성
		String refreshToken = jwtTokenProvider.createRefreshToken(userId);
		
		// Refresh Token 저장
		refreshTokenService.store(userId, refreshToken);
		
		// Refresh Token 쿠키 설정
		ResponseCookie cookie = ResponseCookie.from("rt", refreshToken)
				.httpOnly(true)
				.path("/api")
				.maxAge(60 * 60 * 24 * 14) // 14일
				.build();
		
		response.addHeader("Set-Cookie", cookie.toString());
	}
}
