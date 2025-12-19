package com.ssafy.chillandcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.model.dto.user.LoginRequest;
import com.ssafy.chillandcode.model.dto.user.LoginResponse;
import com.ssafy.chillandcode.model.dto.user.User;
import com.ssafy.chillandcode.model.dto.user.UserSignUpRequest;
import com.ssafy.chillandcode.model.dto.user.UserUpdateRequest;
import com.ssafy.chillandcode.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	// 회원 가입 (등록)
	@PostMapping
	@Operation(summary = "회원가입", description = "사용자 정보를 입력받아 새로운 회원을 등록합니다.")
    public ResponseEntity<ApiResponse<?>> insertUser(@RequestBody UserSignUpRequest req) {
		
		//검증 실패/중복 등은 service에서 예외로 처리됨
		userService.insertUser(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 완료되었습니다.", null));
	}

	// 회원 정보 조회 (프로필 조회)
	@GetMapping("/me")
	@Operation(summary = "회원 정보 조회", description = "로그인된 사용자의 정보를 조회합니다.")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal Long userId) {

		User result = userService.selectUser(userId);
		
        return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 회원 정보 수정
	@PatchMapping("/me")
	@Operation(summary = "회원 정보 수정", description = "로그인된 사용자의 수정 정보를 입력받아 사용자 프로필을 변경합니다.")
    public ResponseEntity<ApiResponse<?>> updateUser(@AuthenticationPrincipal Long userId, @RequestBody UserUpdateRequest req) {
		
		userService.updateUser(userId, req);

        return ResponseEntity.ok(ApiResponse.success("프로필 수정이 완료되었습니다.", null));
	}

	// 회원 정보 삭제 (탈퇴)
	@DeleteMapping("/me")
	@Operation(summary = "회원 탈퇴", description = "로그인된 사용자를 소프트 삭제 처리합니다. 실제 데이터는 삭제되지 않습니다.")
    public ResponseEntity<ApiResponse<?>> deleteUser(@AuthenticationPrincipal Long userId) {
		
		userService.softDelete(userId);

        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴가 완료되었습니다.", null));
	}

	// 로그인
	@PostMapping("/login")
	@Operation(summary = "로그인", description = "이메일과 비밀번호를 입력받아 사용자를 인증합니다.")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest req) {
		LoginResponse result = userService.login(req);

        return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다.", result));
	}

	// 로그아웃
	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "JWT 기반 로그아웃 (클라이언트에서 토큰 폐기)")
    public ResponseEntity<ApiResponse<?>> logout() {
		//서버에서 할 일 없음
		return ResponseEntity.ok(ApiResponse.success("로그아웃이 완료되었습니다.", null));
	}

}
