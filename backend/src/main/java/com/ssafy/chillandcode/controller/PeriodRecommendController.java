package com.ssafy.chillandcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.chillandcode.common.ApiResponse;
import com.ssafy.chillandcode.model.service.RecommendPeriodService;
import com.ssafy.chillcode.recommend.dto.RecommendPeriodRequest;
import com.ssafy.chillcode.recommend.dto.RecommendPeriodResponse;

@RestController
@RequestMapping("/api/recommend")
public class PeriodRecommendController {
	
	@Autowired
	private RecommendPeriodService recommendPeriodService;
	
	@PostMapping("/period")
	public ResponseEntity<?> recommendPeriod(@AuthenticationPrincipal Long userId, @RequestBody RecommendPeriodRequest req){
		RecommendPeriodResponse response = recommendPeriodService.recommendPeriod(userId, req);
		
		return ResponseEntity.ok(ApiResponse.success("기간 추천이 완료되었습니다.", response));
	}
}
