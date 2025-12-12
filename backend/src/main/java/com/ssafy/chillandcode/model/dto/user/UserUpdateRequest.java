package com.ssafy.chillandcode.model.dto.user;

public class UserUpdateRequest {
	private Long userId;
	private String password;
	private String nickname;
	private String region;

	public UserUpdateRequest() {

	}

	public UserUpdateRequest(Long userId, String password, String nickname, String region) {
		super();
		this.userId = userId;
		this.password = password;
		this.nickname = nickname;
		this.region = region;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		return "UserUpdateRequest [userId=" + userId + ", password=" + password + ", nickname=" + nickname + ", region="
				+ region + "]";
	}

}
