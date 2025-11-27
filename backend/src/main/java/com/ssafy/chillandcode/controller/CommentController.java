package com.ssafy.chillandcode.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.chillandcode.model.dto.Comment;
import com.ssafy.chillandcode.model.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 댓글 작성
    @Operation(summary = "댓글 작성", description = "특정 게시글(postId)에 댓글을 작성합니다. userId는 Body로 전달합니다.")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentRequest req) {

        if (req.getContent() == null || req.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "댓글 내용은 비어 있을 수 없습니다."));
        }

        if (req.getUserId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "userId는 필수입니다."));
        }

        Long commentId = commentService.insertComment(postId, req.getUserId(), req.getContent());

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

    // 댓글 수정
    @Operation(summary = "댓글 수정", description = "특정 댓글(commentId)을 수정합니다. 작성자 본인만 수정할 수 있습니다.")
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody UpdateRequest req) {

        if (req.getUserId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "userId는 필수입니다."));
        }

        if (req.getContent() == null || req.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "댓글 내용은 비어 있을 수 없습니다."));
        }

        boolean success = commentService.updateComment(commentId, req.getUserId(), req.getContent());

        if (!success) {
            return ResponseEntity.status(403).body(new MessageResponse(false, "댓글 작성자만 수정할 수 있습니다."));
        }

        return ResponseEntity.ok(new MessageResponse(true, "댓글이 성공적으로 수정되었습니다."));
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "특정 댓글(commentId)을 삭제합니다. 작성자 본인만 삭제할 수 있습니다.")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @RequestBody DeleteRequest req) {

        if (req.getUserId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "userId는 필수입니다."));
        }

        boolean success = commentService.deleteComment(commentId, req.getUserId());

        if (!success) {
            return ResponseEntity.status(403).body(new MessageResponse(false, "댓글 작성자만 삭제할 수 있습니다."));
        }

        return ResponseEntity.ok(new MessageResponse(true, "댓글이 삭제되었습니다."));
    }

    // 내부 DTO 정의
    static class CommentRequest {
        private Long userId;
        private String content;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    static class UpdateRequest {
        private Long userId;
        private String content;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    static class DeleteRequest {
        private Long userId;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }

    static class MessageResponse {
        private boolean success;
        private String message;

        public MessageResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
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

        public Long getCommentId() { return commentId; }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}
