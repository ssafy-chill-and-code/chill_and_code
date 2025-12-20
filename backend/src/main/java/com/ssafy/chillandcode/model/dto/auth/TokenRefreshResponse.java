package com.ssafy.chillandcode.model.dto.auth;

/**
 * refresh 성공 시 내려줄 응답 DTO 
 * - accessToken만 내려준다 (rt는 쿠키로 유지)
 */
public class TokenRefreshResponse {
	private String accessToken;
	
	public TokenRefreshResponse() {

	}

	public TokenRefreshResponse(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public static TokenRefreshResponse of(String accessToken) {
		return new TokenRefreshResponse(accessToken);
	}
}
