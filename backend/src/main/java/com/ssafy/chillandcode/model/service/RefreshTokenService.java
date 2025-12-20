package com.ssafy.chillandcode.model.service;

/**
 * Refresh Token 관리 서비스
 * - store: 로그인 시 refresh token 저장
 * - validate: refresh 요청 시 DB 검증
 * - revoke: 로그아웃 등에 사용 (Step 6에서 제대로 씀)
 */
public interface RefreshTokenService {
	
	void store(Long userId, String refreshTokenRaw);
	
	void validate(String refreshTokenRaw);
	
	void revoke(String refreshTokenRaw);
	
	void revokeAllByUserId(Long userId);
}
