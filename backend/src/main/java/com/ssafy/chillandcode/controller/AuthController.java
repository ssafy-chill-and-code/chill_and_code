package com.ssafy.chillandcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dto.auth.TokenRefreshResponse;
import com.ssafy.chillandcode.model.service.RefreshTokenService;
import com.ssafy.chillandcode.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<TokenRefreshResponse>> refresh(@CookieValue(value = "rt", required = false) String refreshToken){
		
		// 쿠키가 없으면 재발급 불가
		if(refreshToken == null || refreshToken.isBlank()) {
			throw new ApiException(ErrorCode.UNAUTHORIZED, "Refresh Token 쿠키가 없습니다.");
		}
		
		// Refresh Token 검증
		refreshTokenService.validate(refreshToken);
		
		// rt에서 userId 추출
		Long userId = jwtTokenProvider.getUserIdFromRefreshToken(refreshToken);
		
		// 새로운 access token 발급
		String newAccessToken = jwtTokenProvider.createAccessToken(userId);
		
		TokenRefreshResponse data = TokenRefreshResponse.of(newAccessToken);
		return ResponseEntity.ok(ApiResponse.success("Access Token이 재발급되었습니다.", data));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(
				@CookieValue(value = "rt", required = false) String refreshToken
	){
		// 쿠키가 없으면 이미 로그아웃으로 판단
		if(refreshToken == null || refreshToken.isBlank()) {
			throw new ApiException(ErrorCode.UNAUTHORIZED, "이미 로그아웃 상태입니다.");
		}
		
		// DB 기준 refresh token 폐기
		refreshTokenService.revoke(refreshToken);
		
		// 쿠키 삭제
		ResponseCookie deleteCookie = ResponseCookie.from("rt", "")
				.httpOnly(true)
				.path("/api")
				.maxAge(0) // 즉시만료
				.build();
		
		return ResponseEntity.ok()
				.header("Set-Cookie", deleteCookie.toString())
				.body(ApiResponse.success("로그아웃되었습니다.", null));
	}

}
