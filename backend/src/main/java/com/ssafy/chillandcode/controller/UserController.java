package com.ssafy.chillandcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dto.user.User;
import com.ssafy.chillandcode.model.dto.user.UserSignUpRequest;
import com.ssafy.chillandcode.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;

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
    public ResponseEntity<?> getUser(HttpSession session) {

		Long userId = (Long) session.getAttribute("userId");

		if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(ErrorCode.UNAUTHORIZED.getDefaultMessage()));
		}

		User result = userService.selectUser(userId);

		if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure("프로필 조회에 실패했습니다."));
		}

        return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 회원 정보 수정
	@PatchMapping("/me")
	@Operation(summary = "회원 정보 수정", description = "로그인된 사용자의 수정 정보를 입력받아 사용자 프로필을 변경합니다.")
    public ResponseEntity<ApiResponse<?>> updateUser(@RequestBody User user, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");

		if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(ErrorCode.UNAUTHORIZED.getDefaultMessage()));
		}

		user.setUserId(userId);

		boolean result = userService.updateUser(user);

		if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure("프로필 수정에 실패했습니다."));
		}

        return ResponseEntity.ok(ApiResponse.success("프로필 수정이 완료되었습니다.", null));
	}

	// 회원 정보 삭제 (탈퇴)
	@DeleteMapping("/me")
	@Operation(summary = "회원 탈퇴", description = "로그인된 사용자를 소프트 삭제 처리합니다. 실제 데이터는 삭제되지 않습니다.")
    public ResponseEntity<ApiResponse<?>> deleteUser(HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");

		if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(ErrorCode.UNAUTHORIZED.getDefaultMessage()));
		}
		
		boolean result = userService.softDelete(userId);

		if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.failure("회원 탈퇴에 실패했습니다."));
		}
		
		session.invalidate();
		
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴가 완료되었습니다.", null));
	}

	// 로그인 (세션 기반)
	@PostMapping("/login")
	@Operation(summary = "로그인", description = "이메일과 비밀번호를 입력받아 사용자를 인증합니다.")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody User user, HttpSession session) {
		User result = userService.login(user);

		if (result == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure("로그인에 실패했습니다."));
		}

		session.setAttribute("userId", result.getUserId());

        return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다.", null));
	}

	// 로그아웃
	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "로그인 세션을 종료합니다.")
    public ResponseEntity<ApiResponse<?>> logout(HttpSession session) {
		session.invalidate();
        return ResponseEntity.ok(ApiResponse.success("로그아웃이 완료되었습니다.", null));
	}

}
