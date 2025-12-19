package com.ssafy.chillandcode.security.jwt;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
	
	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		log.info("JWT filter hit, uri={}", request.getRequestURI());
		
		// 1. Authorization 헤더 확인
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			
			// 2. 토큰 추출
			String token = authHeader.substring(7);
			
			// 3. 토큰 검증
			if(jwtTokenProvider.validateToken(token)) {
				// 4. userId 추출
				Long userId = jwtTokenProvider.getUserId(token);
				
				// 5. Authentication 만들기 (userId만 principal)
				Authentication auth = new UsernamePasswordAuthenticationToken(
							userId,
							null,
							Collections.emptyList()
						);
				
				// 6. SecurityContext에 저장
				SecurityContextHolder.getContext().setAuthentication(auth);
				
			}else {
				// 실패하면 비워둠 (401은 인가 단계에서 발생)
				SecurityContextHolder.clearContext();
			}
			
		}
		
		
		
		// 다음 필터로 넘김
		filterChain.doFilter(request, response);
	}

}
