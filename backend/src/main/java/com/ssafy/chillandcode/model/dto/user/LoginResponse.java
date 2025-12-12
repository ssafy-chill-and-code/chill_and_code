package com.ssafy.chillandcode.model.dto.user;

public class LoginResponse {
	private Long userId;
	private String nickname;
	private String region;

	public LoginResponse() {
	
	}

	public LoginResponse(Long userId, String nickname, String region) {
		super();
		this.userId = userId;
		this.nickname = nickname;
		this.region = region;
	}
	
	public static LoginResponse from(User user) {
		LoginResponse res = new LoginResponse();
		res.setUserId(user.getUserId());
		res.setNickname(user.getNickname());
		res.setRegion(user.getRegion());
		return res;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "LoginResponse [userId=" + userId + ", nickname=" + nickname + ", region=" + region + "]";
	}

}
