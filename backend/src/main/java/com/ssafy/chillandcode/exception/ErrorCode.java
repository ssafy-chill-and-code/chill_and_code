package com.ssafy.chillandcode.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	
	//4xx
	INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "올바른 이메일 형식이 아닙니다."),
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 8자 이상이며 공백이 없어야 합니다."),
	INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "닉네임은 2~10자이며 공백이 없어야 합니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
	INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
	DELETED_USER(HttpStatus.UNAUTHORIZED, "탈퇴한 회원입니다."),
	
	//5xx
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");
	
	private final HttpStatus status;
	private final String defaultMessage;
	
	private ErrorCode(HttpStatus status, String defaultMessage) {
		this.status = status;
		this.defaultMessage = defaultMessage;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
	
	
}
