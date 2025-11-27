package com.ssafy.chillandcode.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.model.dto.Post;
import com.ssafy.chillandcode.model.service.PostService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostService postService;

	// 게시글 등록
	@PostMapping("/posts")
	@Operation(summary = "게시글 등록", description = "새 게시글을 작성합니다.")
	public Map<String, Object> write(@RequestBody Post post) {
		int result = postService.insert(post);

		Map<String, Object> response = new HashMap<>();
		response.put("postId", post.getPostId());
		response.put("success", result == 1);
		response.put("message", result == 1 ? "게시글이 성공적으로 등록되었습니다." : "게시글 등록에 실패했습니다.");

		return response;
	}

	// 게시글 목록 조회 API
	@Operation(summary = "게시글 목록 조회", description = "전체 또는 지역별 게시글을 조회합니다.")
	@GetMapping
	public List<Post> list(@RequestParam(required = false) String region) {
		return postService.selectAll(region);
	}

	// 게시글 상세 조회 API
	@Operation(summary = "게시글 상세 조회", description = "특정 게시글의 상세 정보를 조회합니다.")
	@GetMapping("/posts/{postId}")
	public ResponseEntity<?> detail(@PathVariable Long postId) {

		Post post = postService.selectById(postId);

		if (post == null) {
			// 404 응답
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("success", false, "message", "해당 게시글을 찾을 수 없습니다."));
		}

		// 정상 응답
		return ResponseEntity.ok(post);
	}

	// 게시글 수정 API
	@Operation(summary = "게시글 수정", description = "특정 게시글의 제목/내용/지역을 수정합니다.")
	@PatchMapping("/posts/{postId}")
	public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody Post post) {

	    // 1. 기존 게시글 조회
	    Post original = postService.selectById(postId);

	    if (original == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("success", false, "message", "해당 게시글을 찾을 수 없습니다."));
	    }

	    // 요청자 ID (MVP에서는 하드코딩 / 나중에는 JWT에서 가져오게 됨)
	    Long requestUserId = 2L; // 테스트: 작성자가 아닌 사람

	    if (!original.getUserId().equals(requestUserId)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Map.of("success", false, "message", "게시글 작성자만 수정할 수 있습니다."));
	    }

	    post.setPostId(postId);
	    int result = postService.update(post);

	    return ResponseEntity.ok(
	            Map.of("success", true, "message", "게시글이 성공적으로 수정되었습니다.")
	    );
	}

	//게시글 삭제
	@Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다. (작성자 본인만 가능)")
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable Long postId) {

	    Post original = postService.selectById(postId);

	    if (original == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("success", false, "message", "해당 게시글을 찾을 수 없습니다."));
	    }

	    // 요청자 ID (MVP에서는 하드코딩. 나중에는 JWT에서 꺼낼 예정)
	    Long requestUserId = 1L;  // 🔥 테스트 시: 작성자로 설정

	    if (!original.getUserId().equals(requestUserId)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Map.of("success", false, "message", "게시글 작성자만 삭제할 수 있습니다."));
	    }

	    int result = postService.delete(postId);

	    if (result == 1) {
	        return ResponseEntity.ok(
	                Map.of("success", true, "message", "게시글이 삭제되었습니다.")
	        );
	    }

	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(Map.of("success", false, "message", "게시글 삭제에 실패했습니다."));
	}


}
