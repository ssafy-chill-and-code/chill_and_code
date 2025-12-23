package com.ssafy.chillandcode.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.model.service.LikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@Tag(name = "Like", description = "게시글 좋아요 API")
public class LikeController {

    @Autowired
    private LikeService likeService;

    /**
     * 좋아요 토글 (추가/취소)
     */
    @PostMapping
    @Operation(summary = "좋아요 토글", description = "게시글에 좋아요를 추가하거나 취소합니다.")
    public ResponseEntity<?> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId) {
        
        boolean isLiked = likeService.toggleLike(postId, userId);
        int likeCount = likeService.getLikeCount(postId);
        
        return ResponseEntity.ok(ApiResponse.success(
            isLiked ? "좋아요를 눌렀습니다." : "좋아요를 취소했습니다.",
            Map.of(
                "isLiked", isLiked,
                "likeCount", likeCount
            )
        ));
    }

    /**
     * 좋아요 여부 및 개수 조회
     */
    @GetMapping
    @Operation(summary = "좋아요 정보 조회", description = "게시글의 좋아요 여부와 개수를 조회합니다.")
    public ResponseEntity<?> getLikeInfo(
            @PathVariable Long postId,
            @AuthenticationPrincipal Long userId) {
        
        boolean isLiked = userId != null && likeService.isLiked(postId, userId);
        int likeCount = likeService.getLikeCount(postId);
        
        return ResponseEntity.ok(ApiResponse.success(
            Map.of(
                "isLiked", isLiked,
                "likeCount", likeCount
            )
        ));
    }
}

