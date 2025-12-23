package com.ssafy.chillandcode.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.ssafy.chillandcode.model.dto.post.Post;
import com.ssafy.chillandcode.model.dto.post.RegionRank;
import com.ssafy.chillandcode.model.dto.post.HashtagRank;
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
    public ResponseEntity<?> write(@AuthenticationPrincipal Long userId, @RequestBody Post post) {

		post.setUserId(userId);

        postService.insert(post);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("게시글이 성공적으로 등록되었습니다.", Map.of("postId", post.getPostId())));
	}

	// 게시글 목록 조회
	@GetMapping("/posts")
	@Operation(summary = "게시글 목록 조회", description = "지역/검색/정렬/페이징 기반 게시글 목록 조회")
    public ResponseEntity<?> list(@RequestParam(required = false) String region,
            @RequestParam(defaultValue = "latest") String sort, @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {

		Map<String, Object> params = new HashMap<>();
		params.put("region", region);
		params.put("sort", sort);
		params.put("search", search);
		params.put("offset", (page - 1) * size);
		params.put("limit", size);

		List<Post> posts = postService.selectAll(params);
		int total = postService.countAll(params);
		int totalPages = (int) Math.ceil((double) total / size);

        return ResponseEntity.ok(ApiResponse.success(Map.of(
        		"posts", posts,
        		"total", total,
        		"totalPages", totalPages,
        		"currentPage", page,
        		"size", size
        )));
	}

	// 지역별 게시글 수 랭킹 조회 (windowDays: 최근 N일, limit: 최대 개수)
	@GetMapping("/posts/region-rank")
	@Operation(summary = "지역별 랭킹", description = "최근 N일 간 지역별 게시글 수 Top N")
    public ResponseEntity<?> regionRank(
            @RequestParam(required = false) Integer windowDays,
            @RequestParam(defaultValue = "5") Integer limit
    ) {

		Map<String, Object> params = new HashMap<>();
		params.put("windowDays", windowDays); // null이면 전체 기간
		params.put("limit", limit);

		List<RegionRank> ranks = postService.selectRegionRank(params);
		return ResponseEntity.ok(ApiResponse.success(Map.of("ranks", ranks)));
	}

	// 해시태그별 게시글 수 랭킹 조회 (windowDays: 최근 N일, limit: 최대 개수)
	@GetMapping("/posts/hashtag-rank")
	@Operation(summary = "해시태그 랭킹", description = "최근 N일 간 해시태그별 게시글 수 Top N")
    public ResponseEntity<?> hashtagRank(
            @RequestParam(required = false) Integer windowDays,
            @RequestParam(defaultValue = "10") Integer limit
    ) {

		Map<String, Object> params = new HashMap<>();
		params.put("windowDays", windowDays); // null이면 전체 기간
		params.put("limit", limit);

		List<HashtagRank> ranks = postService.selectHashtagRank(params);
		return ResponseEntity.ok(ApiResponse.success(Map.of("ranks", ranks)));
	}

	// 내가 쓴 게시글 조회
	@GetMapping("/posts/my")
	@Operation(summary = "내가 쓴 게시글 조회", description = "현재 로그인한 사용자가 작성한 모든 게시글을 조회합니다.")
    public ResponseEntity<?> myPosts(@AuthenticationPrincipal Long userId) {

		List<Post> posts = postService.findByUserId(userId);

        return ResponseEntity.ok(ApiResponse.success(Map.of("posts", posts)));
	}

	// 게시글 상세 조회
	@GetMapping("/posts/{postId}")
	@Operation(summary = "게시글 상세 조회", description = "특정 게시글의 상세 정보를 조회합니다.")
    public ResponseEntity<?> detail(@PathVariable Long postId) {

		Post post = postService.selectById(postId);

        return ResponseEntity.ok(ApiResponse.success(post));
	}

	// 게시글 수정
	@PatchMapping("/posts/{postId}")
	@Operation(summary = "게시글 수정", description = "특정 게시글의 제목/내용/지역을 수정합니다.")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal Long userId, @PathVariable Long postId, @RequestBody Post post) {

		post.setPostId(postId);
		postService.update(post, userId);

        return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 수정되었습니다.", null));
	}

	// 게시글 삭제
	@DeleteMapping("/posts/{postId}")
	@Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다. (작성자 본인만 가능)")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal Long userId, @PathVariable Long postId) {

		postService.delete(postId, userId);

        return ResponseEntity.ok(ApiResponse.success("게시글이 삭제되었습니다.", null));
	}

}
