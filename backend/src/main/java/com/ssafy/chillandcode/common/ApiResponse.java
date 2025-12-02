package com.ssafy.chillandcode.common;

public class ApiResponse<T> {
	private boolean success;
	private String message;
	private T data;
	
	public ApiResponse() {
		
	}

	public ApiResponse(boolean success, String message, T data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public static <T> ApiResponse<T> success(T data){
		return new ApiResponse<>(true, "요청 성공", data);
	}
	
	public static <T> ApiResponse<T> success(String message, T data){
		return new ApiResponse<>(true, message, data);
	}
	
	public static ApiResponse<?> failure(String message){
		return new ApiResponse<>(false, message, null);
	}
}
