package com.ssafy.chillandcode.model.dto.user;

import com.ssafy.chillandcode.model.dto.user.User;

public class UserSignUpRequest {

	private String email; // 로그인 아이디
	private String password; // 비밀번호
	private String nickname; // 닉네임
	private String region; // 사용자 설정 지역
	
	public UserSignUpRequest() {

	}

	public UserSignUpRequest(String email, String password, String nickname, String region) {
		super();
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.region = region;
	}

	//getter setter
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
	
	//DTO - Entity 변환
	public User toEntity() {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setNickname(nickname);
		user.setRegion(region);
		
		return user;
	}
	

}
