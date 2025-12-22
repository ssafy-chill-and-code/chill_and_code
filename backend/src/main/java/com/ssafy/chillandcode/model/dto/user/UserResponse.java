package com.ssafy.chillandcode.model.dto.user;

public class UserResponse {
	private Long userId;
	private String email;
	private String nickname;
	private String region;
	private String profileImageUrl;

	public UserResponse() {

	}

	public UserResponse(Long userId, String email, String nickname, String region, String profileImageUrl) {
		super();
		this.userId = userId;
		this.email = email;
		this.nickname = nickname;
		this.region = region;
		this.profileImageUrl = profileImageUrl;
	}
	
	
	public static UserResponse from(User user) {
		UserResponse res = new UserResponse();
		res.userId = user.getUserId();
		res.email = user.getEmail();
		res.nickname = user.getNickname();
		res.region = user.getRegion();
		res.profileImageUrl = user.getProfileImageUrl();
		
		return res;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getprofileImageUrl() {
		return profileImageUrl;
	}

	public void setprofileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	@Override
	public String toString() {
		return "UserResponse [userId=" + userId + ", email=" + email + ", nickname=" + nickname + ", region=" + region
				+ ", profileImageUrl=" + profileImageUrl + "]";
	}

}
