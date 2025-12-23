package com.ssafy.chillandcode.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.model.dto.comment.CommentCreateRequest;
import com.ssafy.chillandcode.model.dto.comment.CommentResponse;
import com.ssafy.chillandcode.model.dto.comment.CommentUpdateRequest;
import com.ssafy.chillandcode.model.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class CommentController {

	@Autowired
	private CommentService commentService;

	// 댓글 작성
	@PostMapping("/posts/{postId}/comments")
	@Operation(summary = "댓글 등록", description = "특정 게시글(postId)에 댓글을 등록합니다.")
	public ResponseEntity<?> createComment(@AuthenticationPrincipal Long userId, @PathVariable Long postId,
			@RequestBody CommentCreateRequest req) {

		// 서비스 호출 전 입력 형식 검증
		if (req.getContent() == null || req.getContent().trim().isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("댓글 내용은 비어 있을 수 없습니다."));
		}

		Long commentId = commentService.insertComment(postId, userId, req.getContent());

		return ResponseEntity.ok(ApiResponse.success("댓글이 성공적으로 등록되었습니다.", Map.of("commentId", commentId)));
	}

	// 댓글 목록 조회
	@GetMapping("/posts/{postId}/comments")
	@Operation(summary = "댓글 목록 조회", description = "특정 게시글에 달린 모든 댓글 목록을 조회합니다.")
	public ResponseEntity<?> getComments(@PathVariable Long postId) {

		List<CommentResponse> comments = commentService.findCommentsByPostId(postId);

		return ResponseEntity.ok(ApiResponse.success(Map.of("comments", comments)));
	}

	// 내가 쓴 댓글 목록 조회
	@GetMapping("/comments/my")
	@Operation(summary = "내가 쓴 댓글 조회", description = "현재 로그인한 사용자가 작성한 모든 댓글을 조회합니다.")
	public ResponseEntity<?> myComments(@AuthenticationPrincipal Long userId) {

		return ResponseEntity.ok(ApiResponse.success(Map.of("comments", commentService.findByUserId(userId))));
	}

	// 댓글 수정
	@PatchMapping("/comments/{commentId}")
	@Operation(summary = "댓글 수정", description = "특정 댓글(commentId)을 수정합니다. 작성자 본인만 수정할 수 있습니다.")
	public ResponseEntity<?> updateComment(@AuthenticationPrincipal Long userId, @PathVariable Long commentId,
			@RequestBody CommentUpdateRequest req) {

		// 서비스 호출 전 입력 형식 검증
		if (req.getContent() == null || req.getContent().trim().isEmpty()) {
			return ResponseEntity.badRequest().body(ApiResponse.failure("댓글 내용은 비어 있을 수 없습니다."));
		}

		commentService.updateComment(commentId, userId, req.getContent());

		return ResponseEntity.ok(ApiResponse.success("댓글이 성공적으로 수정되었습니다.", null));
	}

	// 댓글 삭제
	@DeleteMapping("/comments/{commentId}")
	@Operation(summary = "댓글 삭제", description = "특정 댓글(commentId)을 삭제합니다. 작성자 본인만 삭제할 수 있습니다.")
	public ResponseEntity<?> deleteComment(@AuthenticationPrincipal Long userId, @PathVariable Long commentId) {

		commentService.deleteComment(commentId, userId);

		return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다.", null));
	}

}
