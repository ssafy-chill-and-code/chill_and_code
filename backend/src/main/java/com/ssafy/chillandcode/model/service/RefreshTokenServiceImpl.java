package com.ssafy.chillandcode.model.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dao.RefreshTokenDao;
import com.ssafy.chillandcode.model.dto.auth.RefreshToken;
import com.ssafy.chillandcode.security.jwt.JwtTokenProvider;

/**
 * Refresh Token 관리 서비스 구현체
 *
 * 책임:
 * 1) 로그인 시 Refresh Token을 DB에 저장 (hash 형태)
 * 2) Refresh 요청 시 DB 기준으로 유효성 검증
 * 3) 로그아웃/강제 만료를 위한 revoke 처리
 *
 */

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
	
	@Autowired
	private RefreshTokenDao refreshTokenDao;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	// refresh token 만료 기간(ms) - JWT 만료와 같은 값으로 맞추는게 안전
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpirationMs;
	
	//로그인 성공 시 Refresh Token 저장
	@Override
	@Transactional
	public void store(Long userId, String refreshTokenRaw) {
		//(default) 멀티 디바이스 허용 안 함
		refreshTokenDao.revokeAllByUserId(userId);
		
		// refresh token 원문 → hash 변환
		String tokenHash = hash(refreshTokenRaw);
		
		// 만료 시각 계산 (JWT 만료와 동일 기준)
		LocalDateTime expiresAt = LocalDateTime.ofInstant(
				Instant.ofEpochMilli(System.currentTimeMillis() + refreshTokenExpirationMs),
                ZoneId.systemDefault()
			);
		
		// DB 저장
		RefreshToken entity = new RefreshToken(userId, tokenHash, expiresAt);
		
		int rows = refreshTokenDao.insert(entity);
		
		if(rows != 1) {
			throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "Refresh Token 저장에 실패했습니다.");
		}
	}
	
	//Refresh 요청 시 Refresh Token 검증
	@Override
	public void validate(String refreshTokenRaw) {
		// 토큰 자체 검증 (JWT가 위조/만료인지)
		boolean ok = jwtTokenProvider.validateToken(refreshTokenRaw);
		
		if(!ok) {
			throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
		}
		
		// DB 조회
		String tokenHash = hash(refreshTokenRaw);
		RefreshToken token = refreshTokenDao.findByTokenHash(tokenHash);
		
		if(token == null) {
			throw new ApiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
		}
		
		// 폐기 토큰 확인
		if(token.isRevoked()) {
			throw new ApiException(ErrorCode.REVOKED_REFRESH_TOKEN);
		}
		
		// DB 기준 만료 여부 확인
		if(token.getExpiresAt().isBefore(LocalDateTime.now())) {
			refreshTokenDao.revokeByTokenHash(tokenHash); //만료 토큰 폐기
			throw new ApiException(ErrorCode.EXPIRED_REFRESH_TOKEN);
		}
	}
	


	// 특정 Refresh 토큰 폐기
	@Override
	@Transactional
	public void revoke(String refreshTokenRaw) {
		String tokenHash = hash(refreshTokenRaw);
		refreshTokenDao.revokeByTokenHash(tokenHash);
	}
	
	// 유저 기준 Refresh Token 전체 폐기
	@Override
	@Transactional
	public void revokeAllByUserId(Long userId) {
		refreshTokenDao.revokeAllByUserId(userId);
	}
	
	 /**
     * SHA-256 해시 함수
     * 목적:
     * - Refresh Token 원문 DB 저장 방지
     * - DB 유출 시 피해 최소화
     */
	private String hash(String raw) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
		} catch(Exception e) {
			 throw new ApiException(
	                    ErrorCode.INTERNAL_SERVER_ERROR,
	                    "Refresh Token 해싱 중 오류가 발생했습니다."
	            );
		}
	}
}
