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
    INVALID_RATING(HttpStatus.BAD_REQUEST, "별점은 1~5 사이의 값이어야 합니다."),
    MISSING_REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),

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
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),
    
    INVALID_SCHEDULE_TIME(HttpStatus.BAD_REQUEST, "시작 시간은 종료 시간보다 늦을 수 없습니다."),
    
    // 🔹 OAuth 인증 / 가입
    OAUTH_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 가입된 이메일로 OAuth 가입을 할 수 없습니다."),
    OAUTH_USER_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth 사용자 생성에 실패했습니다."),
    OAUTH_PROVIDER_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 OAuth 제공자입니다."),
    
    /* =========================
     * 비밀번호 변경
     * ========================= */

    // 🔹 인증(Authentication)
    INVALID_CURRENT_PASSWORD(HttpStatus.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다."),

    // 🔹 인가(Authorization)
    OAUTH_USER_PASSWORD_NOT_ALLOWED(HttpStatus.FORBIDDEN, "OAuth 사용자는 비밀번호를 변경할 수 없습니다."),

    // 🔹 입력값 검증 (Validation)
    INVALID_NEW_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호는 8자 이상이며 공백이 없어야 합니다."),

    // 🔹 서버 오류
    PASSWORD_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 변경에 실패했습니다."),

    
    // 🔹 파일 업로드 관련
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "허용되지 않은 파일 형식입니다."),
    FILE_TOO_LARGE(HttpStatus.BAD_REQUEST, "파일 용량이 너무 큽니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    
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
