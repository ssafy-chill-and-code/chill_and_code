package com.ssafy.chillandcode.security.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.model.dto.user.User;
import com.ssafy.chillandcode.model.service.UserService;
import com.ssafy.chillandcode.security.auth.AuthTokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthTokenService authTokenService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		try {
			
			OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
			
			User user = userService.findOrCreateOAuthUser(oAuth2User);
			
			// 토큰 발급 (일반 로그인과 동일)
			authTokenService.issueTokens(user.getUserId(), response);
			
			// OAuth는 redirect 기반 (프론트엔드로) - 추후 환경변수로 URL 관리
			response.sendRedirect("http://localhost:5173/oauth/callback");
			
		}catch (ApiException e){
			throw new OAuth2AuthenticationException(
						new OAuth2Error(e.getErrorCode().name()),
						e.getMessage(),
						e
					);
		}
		
	}

}
