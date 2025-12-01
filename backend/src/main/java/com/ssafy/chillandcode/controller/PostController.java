package com.ssafy.chillandcode.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.exception.ApiException;
import com.ssafy.chillandcode.exception.ErrorCode;
import com.ssafy.chillandcode.model.dto.Post;
import com.ssafy.chillandcode.model.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostService postService;

	// 게시글 등록
	@PostMapping("/posts")
	@Operation(summary = "게시글 등록", description = "새 게시글을 작성합니다.")
	public ApiResponse<Map<String, Object>> write(@RequestBody Post post, HttpSession session) {

		// 1. 로그인 여부 확인
		Long userId = (Long) session.getAttribute("userId");
		// Swagger 테스트용 임시 하드코딩
		if (userId == null) {
			userId = 1L; // 나중에 제거
		}

		// 2. 작성자 정보 설정
		post.setUserId(userId);

		// 3. DB 저장 (실패 시 Service에서 예외 발생)
		postService.insert(post);

		return ApiResponse.success(Map.of("postId", post.getPostId()));
	}

	// 게시글 목록 조회
	@Operation(summary = "게시글 목록 조회", description = "지역/검색/정렬/페이징 기반 게시글 목록 조회")
	@GetMapping("/posts")
	public ApiResponse<Map<String, Object>> list(@RequestParam(required = false) String region,
			@RequestParam(defaultValue = "latest") String sort, @RequestParam(required = false) String search,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {

		Map<String, Object> params = new HashMap<>();
		params.put("region", region);
		params.put("sort", sort);
		params.put("search", search);
		params.put("offset", (page - 1) * size);
		params.put("limit", size);

		return ApiResponse.success(Map.of("posts", postService.selectAll(params)));
	}

	// 내가 쓴 게시글 조회
	@Operation(summary = "내가 쓴 게시글 조회", description = "현재 로그인한 사용자가 작성한 모든 게시글을 조회합니다.")
	@GetMapping("/posts/my")
	public ApiResponse<Map<String, Object>> myPosts(HttpSession session) {

		Long userId = (Long) session.getAttribute("userId");
		// Swagger 테스트용 fallback
		if (userId == null) {
			userId = 1L;
		}

		List<Post> posts = postService.findByUserId(userId);

		return ApiResponse.success(Map.of("posts", posts));
	}

	// 게시글 상세 조회
	@Operation(summary = "게시글 상세 조회", description = "특정 게시글의 상세 정보를 조회합니다.")
	@GetMapping("/posts/{postId}")
	public ApiResponse<Post> detail(@PathVariable Long postId) {

		Post post = postService.selectById(postId);
		if (post == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}
		return ApiResponse.success(post);
	}

	// 게시글 수정
	@Operation(summary = "게시글 수정", description = "특정 게시글의 제목/내용/지역을 수정합니다.")
	@PatchMapping("/posts/{postId}")
	public ApiResponse<String> updatePost(@PathVariable Long postId, @RequestBody Post post, HttpSession session) {

		// 로그인 체크
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			userId = 1L; // swagger 임시
		}

		// 기존 게시글 조회
		Post original = postService.selectById(postId);
		if (original == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}

		// 작성자 여부 체크(불일치 시 예외)
		if (!original.getUserId().equals(userId)) {
			throw new ApiException(ErrorCode.POST_OWNER_MISMATCH);
		}

		// 수정 실행 (실패 시 Service에서 예외)
		post.setPostId(postId);
		postService.update(post);

		return ApiResponse.success("게시글이 성공적으로 수정되었습니다.", null);
	}

	// 게시글 삭제
	@Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다. (작성자 본인만 가능)")
	@DeleteMapping("/posts/{postId}")
	public ApiResponse<String> deletePost(@PathVariable Long postId, HttpSession session) {

		// 로그인 체크
		Long userId = (Long) session.getAttribute("userId");
		if (userId == null) {
			userId = 1L; // swagger 임시
		}

		Post original = postService.selectById(postId);
		if (original == null) {
			throw new ApiException(ErrorCode.POST_NOT_FOUND);
		}

		// 작성자 여부 체크
		if (!original.getUserId().equals(userId)) {
			throw new ApiException(ErrorCode.POST_OWNER_MISMATCH);
		}

		postService.delete(postId);
		return ApiResponse.success("게시글이 삭제되었습니다.", null);
	}

}
