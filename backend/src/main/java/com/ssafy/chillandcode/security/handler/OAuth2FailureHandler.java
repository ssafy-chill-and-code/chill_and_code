package com.ssafy.chillandcode.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		//기본값 (알 수 없는 실패)
		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
		String message = "OAuth 로그인에 실패했습니다.";
		
		//OAuthSuccessHandler/UserService에서 던진 ApiException 해석
		if(exception.getCause() instanceof ApiException apiException) {
			errorCode = apiException.getErrorCode();
			message = apiException.getMessage();
		}
		
		//errorCode만 전달(직접 메시지 노출X)
		String redirectUrl = "/oauth/callback" + "?errorCode=" + errorCode.name();
		
		response.sendRedirect(redirectUrl);
	}
	

}
