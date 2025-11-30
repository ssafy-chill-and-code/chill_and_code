package com.ssafy.chillandcode.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.chillandcode.model.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.Hidden;

@RestControllerAdvice
@Hidden //swagger 스캔 제외
public class GlobalExceptionHandler {
	
	//ApiException 처리
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException e){
		ErrorCode code = e.getErrorCode();
		ErrorResponse body = new ErrorResponse(code.name(), e.getMessage());
		
		return ResponseEntity.status(code.getStatus()).body(body);
	}
	
	//그 외 오류 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e){
		//로그 남기고
		e.printStackTrace();
		
		ErrorCode code = ErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse body = new ErrorResponse(code.name(), code.getDefaultMessage());
		
		return ResponseEntity.status(code.getStatus()).body(body);
	}
}
