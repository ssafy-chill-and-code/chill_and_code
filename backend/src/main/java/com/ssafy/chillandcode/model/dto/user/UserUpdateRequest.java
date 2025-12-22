package com.ssafy.chillandcode.model.dto.user;

public class UserUpdateRequest {
	private Long userId;
	private String nickname;
	private String region;
	private String profileImageUrl;

	public UserUpdateRequest() {

	}

	public UserUpdateRequest(Long userId, String nickname, String region, String profileImageUrl) {
		super();
		this.userId = userId;
		this.nickname = nickname;
		this.region = region;
		this.profileImageUrl = profileImageUrl;
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

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	@Override
	public String toString() {
		return "UserUpdateRequest [userId=" + userId + ", nickname=" + nickname + ", region=" + region
				+ ", profileImageUrl=" + profileImageUrl + "]";
	}

}
