package com.ssafy.chillandcode.recommend.place;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/recommend")
public class PlaceRecommendController {

	private final PlaceRecommendService placeRecommendService;
	private final LlmClient llmClient;

	public PlaceRecommendController(PlaceRecommendService placeRecommendService, LlmClient llmClient) {
		this.placeRecommendService = placeRecommendService;
		this.llmClient = llmClient;
	}

	@Operation(summary = "장소 추천", description = "style, budget, region, transport 입력을 받아 추천 카드 목록을 반환합니다.")
	@GetMapping("/places")
	public List<PlaceRecommendCard> recommend(@RequestParam String style, @RequestParam String budget,
			@RequestParam(required = false) String region, @RequestParam(required = false) String transport) {
		// 1) LLM 호출 (표현용 데이터)
		Map<Long, LlmResult> llmMap = llmClient.requestLlmResults(style, budget);

		// 2) 추천 카드 생성 (점수/정렬 1회 + LLM 반영)
		return placeRecommendService.recommendCards(style, budget, region, transport, llmMap);
	}

}
