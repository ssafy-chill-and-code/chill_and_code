package com.ssafy.chillandcode.security.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.exception.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 권한 실패 (403)
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		ErrorCode errorCode = ErrorCode.FORBIDDEN_ACTION; // 권한 없음

		response.setStatus(errorCode.getStatus().value()); // HTTP 403
		response.setContentType("application/json;charset=UTF-8");

		ErrorResponse body = new ErrorResponse(errorCode.getStatus().value(), errorCode.name(),
				errorCode.getDefaultMessage());

		response.getWriter().write(objectMapper.writeValueAsString(body));
	}

}
