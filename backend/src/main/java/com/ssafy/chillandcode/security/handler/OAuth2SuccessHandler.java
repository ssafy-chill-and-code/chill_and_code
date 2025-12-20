package com.ssafy.chillandcode.security.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
		
		System.out.println("🔥 OAuth2SuccessHandler 실행됨");
		
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		
		User user = userService.findOrCreateOAuthUser(oAuth2User);
		
		System.out.println("🔥 OAuth 사용자 userId = " + user.getUserId());
		
		// 토큰 발급 (일반 로그인과 동일)
		authTokenService.issueTokens(user.getUserId(), response);
		
		System.out.println("🔥 Refresh Token 쿠키 발급 완료");
		
		// OAuth는 redirect 기반
		response.sendRedirect("/oauth/callback");
	}

}
