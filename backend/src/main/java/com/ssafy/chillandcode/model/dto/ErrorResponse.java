package com.ssafy.chillandcode.model.dto;

public class ErrorResponse {
	private boolean success;
	private String code;
	private String message;
	
	public ErrorResponse(String code, String message) {
		this.success = false;
		this.code = code;
		this.message = message;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
}
