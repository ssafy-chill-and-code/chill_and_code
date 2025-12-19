package com.ssafy.chillandcode.model.dto.user;

public class LoginResponse {
	private Long userId;
	private String accessToken;

	public LoginResponse() {
	
	}

	public LoginResponse(Long userId, String accessToken) {
		super();
		this.userId = userId;
		this.accessToken = accessToken;
	}
	
	public static LoginResponse from(User user, String accessToken) {
		return new LoginResponse(user.getUserId(), accessToken);
	}
	public Long getUserId() {
		return userId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public String toString() {
		return "LoginResponse [userId=" + userId + ", accessToken=" + accessToken + "]";
	}

}
