package com.ssafy.chillandcode.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;

@RestControllerAdvice
@Hidden //swagger 스캔 제외
public class GlobalExceptionHandler {
	
	//ApiException 처리
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException e){
		ErrorCode code = e.getErrorCode();
		
		ErrorResponse response = new ErrorResponse(
				code.getStatus().value(), code.name(), e.getMessage());
		
		return ResponseEntity.status(code.getStatus()).body(response);
	}
	
	//그 외 오류 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e){
		ErrorResponse response = new ErrorResponse(
				ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value(),
				ErrorCode.INTERNAL_SERVER_ERROR.name(),
				e.getMessage()
				);
		
		return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(response);
				
	}
}
