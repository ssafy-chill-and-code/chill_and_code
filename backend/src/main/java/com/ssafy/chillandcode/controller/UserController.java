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

import com.ssafy.chillandcode.model.dto.User;
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
	public ResponseEntity<String> insertUser(@RequestBody User user) {
		boolean result = userService.insertUser(user);

		if (!result) {
			return new ResponseEntity<>("회원가입에 실패했습니다.", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>("회원가입이 완료되었습니다.", HttpStatus.CREATED);
	}

	// 회원 정보 조회 (프로필 조회)
	@GetMapping("/me")
	@Operation(summary = "회원 정보 조회", description = "로그인된 사용자의 정보를 조회합니다.")
	public ResponseEntity<?> getUser(HttpSession session) {

		Long userId = (Long) session.getAttribute("userId");

		if (userId == null) {
			return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		User result = userService.selectUser(userId);

		if (result == null) {
			return new ResponseEntity<String>("프로필 조회에 실패했습니다.", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<User>(result, HttpStatus.OK);
	}

	// 회원 정보 수정
	@PatchMapping("/me")
	@Operation(summary = "회원 정보 수정", description = "로그인된 사용자의 수정 정보를 입력받아 사용자 프로필을 변경합니다.")
	public ResponseEntity<String> updateUser(@RequestBody User user, HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");

		if (userId == null) {
			return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		user.setUserId(userId);

		boolean result = userService.updateUser(user);

		if (!result) {
			return new ResponseEntity<String>("프로필 수정에 실패했습니다.", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>("프로필 수정이 완료되었습니다.", HttpStatus.OK);
	}

	// 회원 정보 삭제 (탈퇴)
	@DeleteMapping("/me")
	@Operation(summary = "회원 탈퇴", description = "로그인된 사용자를 소프트 삭제 처리합니다. 실제 데이터는 삭제되지 않습니다.")
	public ResponseEntity<String> deleteUser(HttpSession session) {
		Long userId = (Long) session.getAttribute("userId");

		if (userId == null) {
			return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}
		
		boolean result = userService.softDelete(userId);

		if (!result) {
			return new ResponseEntity<String>("회원 탈퇴에 실패했습니다.", HttpStatus.NOT_FOUND);
		}
		
		session.invalidate();
		
		return new ResponseEntity<String>("회원 탈퇴가 완료되었습니다.", HttpStatus.OK);
	}

	// 로그인 (세션 기반)
	@PostMapping("/login")
	@Operation(summary = "로그인", description = "이메일과 비밀번호를 입력받아 사용자를 인증합니다.")
	public ResponseEntity<String> login(@RequestBody User user, HttpSession session) {
		User result = userService.login(user);

		if (result == null) {
			return new ResponseEntity<String>("로그인에 실패했습니다.", HttpStatus.UNAUTHORIZED);
		}

		session.setAttribute("userId", result.getUserId());

		return new ResponseEntity<>("로그인이 완료되었습니다.", HttpStatus.OK);
	}

	// 로그아웃
	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "로그인 세션을 종료합니다.")
	public ResponseEntity<String> logout(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<String>("로그아웃이 완료되었습니다.", HttpStatus.OK);
	}

}
