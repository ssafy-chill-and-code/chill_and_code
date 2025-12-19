package com.ssafy.chillandcode.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	
	/* =========================
     * 4xx - Client Error
     * ========================= */

    // 🔹 입력값 검증 (Validation)
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "올바른 이메일 형식이 아닙니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 8자 이상이며 공백이 없어야 합니다."),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "닉네임은 2~10자이며 공백이 없어야 합니다."),

    // 🔹 중복 / 상태 검증
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    DELETED_USER(HttpStatus.UNAUTHORIZED, "탈퇴한 회원입니다."),

    // 🔹 인증(Authentication)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    
    // 인증 / 토큰 관련
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 Refresh Token입니다."),
    REVOKED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "폐기된 Refresh Token입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh Token을 찾을 수 없습니다."),
    
    // 🔹 인가(Authorization)
    FORBIDDEN_ACTION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    
    // 🔹 리소스 조회 실패 (Not Found)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),
    
    INVALID_SCHEDULE_TIME(HttpStatus.BAD_REQUEST, "시작 시간은 종료 시간보다 늦을 수 없습니다."),


    /* =========================
     * 5xx - Server Error
     * ========================= */

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
	USER_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "회원 정보 수정에 실패했습니다."),
	USER_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "회원 탈퇴 처리에 실패했습니다.");
	
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
