package com.ssafy.chillandcode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ssafy.chillandcode.security.jwt.JwtAuthenticationFilter;
import com.ssafy.chillandcode.security.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		//만든 JWT 필터 생성
		JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtTokenProvider);
		
		
		http
			// CSRF 비활성화
			.csrf(csrf -> csrf.disable())
			
			// 세션 사용 안 함 (STATELESS)
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			
			// 요청별 접근 권한 설정
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
					.requestMatchers(HttpMethod.POST, "/api/users", "/api/users/login").permitAll()
					
					// 게시글 조회: 내 글 이외에는 누구나 접근 가능
					.requestMatchers(HttpMethod.GET, "/api/posts/my").authenticated()
					.requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/*", "/api/posts/*/comments").permitAll()
					
					// 게시글 및 댓글 쓰기/수정/삭제
					.requestMatchers(HttpMethod.POST, "/api/posts").authenticated()
					.requestMatchers(HttpMethod.PATCH, "/api/posts/*").authenticated()
					.requestMatchers(HttpMethod.DELETE, "/api/posts/*").authenticated()
					.requestMatchers(HttpMethod.POST, "/api/posts/*/comments").authenticated()
					.requestMatchers(HttpMethod.GET, "/api/comments/my").authenticated()
					.requestMatchers(HttpMethod.PATCH, "/api/comments/*").authenticated()
					.requestMatchers(HttpMethod.DELETE, "/api/comments/*").authenticated()
					
					// 일정, 추천, 사용자 정보 등 민감 API
					.requestMatchers("/api/schedules/**").authenticated()
					.requestMatchers("/api/recommend/**").authenticated()
					.requestMatchers("/api/users/me").authenticated()
					.requestMatchers(HttpMethod.POST, "/api/users/logout").authenticated()
					
					.anyRequest().authenticated()
			)
			
			// EntryPoint 설정 (토큰 없으면 401로 통일)
			.exceptionHandling(eh -> eh
					.authenticationEntryPoint((req, res, ex) -> {
						res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
						res.setContentType("application/json;charset=UTF-8");
						res.getWriter().write("{\"success\":false,\"message\":\"Unauthorized\"}");
					})
					)
			
			// JWT 인증 필터를 Security 필터 체인에 등록
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			
			// 기본 로그인 인증 비활성화
			.formLogin(form -> form.disable()).httpBasic(basic -> basic.disable());

		return http.build();
	}
}
