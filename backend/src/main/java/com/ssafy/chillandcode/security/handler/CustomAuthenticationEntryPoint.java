package com.ssafy.chillandcode.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.exception.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 인증 실패 담당 (401)
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	// ErrorResponse -> JSON 변환기
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		ErrorCode errorCode = ErrorCode.UNAUTHORIZED; // 인증 실패

		response.setStatus(errorCode.getStatus().value()); // HTTP 상태 코드 = 401
		response.setContentType("application/json;charset=UTF-8"); // JSON 응답 명시

		ErrorResponse body = new ErrorResponse(errorCode.getStatus().value(), errorCode.name(),
				errorCode.getDefaultMessage());
		
		//JSON 문자열을 response body에 작성
		response.getWriter().write(objectMapper.writeValueAsString(body));

	}

}
