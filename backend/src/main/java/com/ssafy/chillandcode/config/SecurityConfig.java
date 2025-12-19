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
					//인증 없이 허용할 API
					.requestMatchers(HttpMethod.POST, "/api/users").permitAll()	//회원가입
					.requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()	//로그인
					.requestMatchers(HttpMethod.GET, "/api/posts").permitAll()	//게시글 조회
					.requestMatchers(HttpMethod.GET, "/api/posts/{postId}").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/posts/{postId}/comments").permitAll() // 댓글 조회
					.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
					
					//나머지는 모두 인증 필요
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
