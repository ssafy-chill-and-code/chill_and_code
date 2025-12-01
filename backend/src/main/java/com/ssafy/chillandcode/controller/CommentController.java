package com.ssafy.chillandcode.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.model.dto.Comment;
import com.ssafy.chillandcode.model.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class CommentController {

	@Autowired
	private CommentService commentService;

	// 댓글 작성
	@Operation(summary = "댓글 등록", description = "특정 게시글(postId)에 댓글을 등록합니다.")
	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentRequest req,
			HttpSession session) {

		// 로그인 사용자 확인
		Long userId = (Long) session.getAttribute("userId");

		// Swagger 테스트용 fallback
		if (userId == null) {
			userId = 1L;
		}

		// 댓글 내용 검증
		if (req.getContent() == null || req.getContent().trim().isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse(false, "댓글 내용은 비어 있을 수 없습니다."));
		}

		// 서비스 호출
		Long commentId = commentService.insertComment(postId, userId, req.getContent());

		if (commentId == null) {
			return ResponseEntity.status(404).body(new MessageResponse(false, "해당 게시글을 찾을 수 없습니다."));
		}

		return ResponseEntity.ok(new CommentCreateResponse(commentId, true, "댓글이 성공적으로 등록되었습니다."));
	}

	// 댓글 목록 조회
	@Operation(summary = "댓글 목록 조회", description = "특정 게시글에 달린 모든 댓글 목록을 조회합니다.")
	@GetMapping("/posts/{postId}/comments")
	public ResponseEntity<?> getComments(@PathVariable Long postId) {

		List<Comment> comments = commentService.findCommentsByPostId(postId);

		if (comments == null) {
			return ResponseEntity.status(404).body(new MessageResponse(false, "해당 게시글을 찾을 수 없습니다."));
		}

		HashMap<String, Object> result = new HashMap<>();
		result.put("comments", comments);
		return ResponseEntity.ok(result);
	}

	// 내가 쓴 댓글 목록 조회
	@Operation(summary = "내가 쓴 댓글 조회", description = "현재 로그인한 사용자가 작성한 모든 댓글을 조회합니다.")
	@GetMapping("/comments/my")
	public ResponseEntity<?> myComments(HttpSession session) {

		Long userId = (Long) session.getAttribute("userId");

		// Swagger 테스트용 fallback
		if (userId == null) {
			userId = 1L;
		}

		return ResponseEntity.ok(Map.of("comments", commentService.findByUserId(userId)));
	}

	// 댓글 수정
	@Operation(summary = "댓글 수정", description = "특정 댓글(commentId)을 수정합니다. 작성자 본인만 수정할 수 있습니다.")
	@PatchMapping("/comments/{commentId}")
	public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody UpdateRequest req,
			HttpSession session) {

		Long userId = (Long) session.getAttribute("userId");

		// Swagger fallback
		if (userId == null) {
			userId = 1L;
		}

		// 내용 검증
		if (req.getContent() == null || req.getContent().trim().isEmpty()) {
			return ResponseEntity.badRequest().body(new MessageResponse(false, "댓글 내용은 비어 있을 수 없습니다."));
		}

		// 작성자 검증은 service 내부에서 처리됨
		boolean success = commentService.updateComment(commentId, userId, req.getContent());

		if (!success) {
			return ResponseEntity.status(403).body(new MessageResponse(false, "댓글 작성자만 수정할 수 있습니다."));
		}

		return ResponseEntity.ok(new MessageResponse(true, "댓글이 성공적으로 수정되었습니다."));
	}

	// 댓글 삭제
	@Operation(summary = "댓글 삭제", description = "특정 댓글(commentId)을 삭제합니다. 작성자 본인만 삭제할 수 있습니다.")
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable Long commentId, HttpSession session) {

		Long userId = (Long) session.getAttribute("userId");

		// Swagger fallback
		if (userId == null) {
			userId = 1L;
		}

		// 작성자 검증은 service 내부에서 처리됨
		boolean success = commentService.deleteComment(commentId, userId);

		if (!success) {
			return ResponseEntity.status(403).body(new MessageResponse(false, "댓글 작성자만 삭제할 수 있습니다."));
		}

		return ResponseEntity.ok(new MessageResponse(true, "댓글이 삭제되었습니다."));
	}

	// 내부 DTO 정의 (userId 제거)
	static class CommentRequest {
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	static class UpdateRequest {
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	static class MessageResponse {
		private boolean success;
		private String message;

		public MessageResponse(boolean success, String message) {
			this.success = success;
			this.message = message;
		}

		public boolean isSuccess() {
			return success;
		}

		public String getMessage() {
			return message;
		}
	}

	static class CommentCreateResponse {
		private Long commentId;
		private boolean success;
		private String message;

		public CommentCreateResponse(Long commentId, boolean success, String message) {
			this.commentId = commentId;
			this.success = success;
			this.message = message;
		}

		public Long getCommentId() {
			return commentId;
		}

		public boolean isSuccess() {
			return success;
		}

		public String getMessage() {
			return message;
		}
	}
}
