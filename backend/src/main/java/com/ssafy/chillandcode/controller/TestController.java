package com.ssafy.chillandcode.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;

@RestController
public class TestController {
	
	//정상 응답 테스트
	@GetMapping("/test/success")
	public ApiResponse<String> successTest(){
		return ApiResponse.success("테스트 성공");
	}
	
	//예외 응답 테스트
	@GetMapping("/test/error")
	public ApiResponse<String> errorTest(){
		throw new ApiException(ErrorCode.INVALID_EMAIL_FORMAT);
	}
}
