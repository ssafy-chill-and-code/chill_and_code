package com.ssafy.chillandcode.model.dto.user;

/**
 * User는 회원 기본 정보를 담는 DTO 입니다.
 */

public class User {
	private Long userId; // 회원ID (PK)
	private String email; // 회원 식별용 이메일 (로그인ID)
	private String password; // 로그인 비밀번호
	private String nickname; // 서비스 내 표시 이름
	private String region; // 사용자 설정 지역
	private String createdAt; // 가입 일자
	private int isDeleted; // 회원 탈퇴 여부 (0=정상, 1=탈퇴)
	private String deletedAt; // 탈퇴 처리 시각
	private String provider;	//OAuth 제공자
	private String providerId;	//OAuth 제공 고유 사용자 ID
	
	public User() {
	}

	public User(Long userId, String email, String password, String nickname, String region, String createdAt,
			int isDeleted, String deletedAt, String provider, String providerId) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.region = region;
		this.createdAt = createdAt;
		this.isDeleted = isDeleted;
		this.deletedAt = deletedAt;
		this.provider = provider;
		this.providerId = providerId;
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(String deletedAt) {
		this.deletedAt = deletedAt;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", email=" + email + ", password=" + password + ", nickname=" + nickname
				+ ", region=" + region + ", createdAt=" + createdAt + ", isDeleted=" + isDeleted + ", deletedAt="
				+ deletedAt + ", provider=" + provider + ", providerId=" + providerId + "]";
	}

}
