package com.ssafy.chillandcode.recommend.place;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

/**
 * 장소 추천 API 컨트롤러.
 * - 인증 후 사용자가 전달한 style/budget/region/transport를 받아
 *   서비스에서 점수 계산/정렬 및 LLM 반영을 수행한 카드 목록을 반환한다.
 */
@RestController
@RequestMapping("/api/recommend")
public class PlaceRecommendController {

    private final PlaceRecommendService placeRecommendService;

    /** 서비스 주입 */
    public PlaceRecommendController(PlaceRecommendService placeRecommendService) {
        this.placeRecommendService = placeRecommendService;
    }

	@Operation(summary = "장소 추천", description = "style, budget, region, transport 입력을 받아 추천 카드 목록을 반환합니다.")
	@GetMapping("/places")
    public List<PlaceRecommendCard> recommend(@RequestParam String style, @RequestParam String budget,
                @RequestParam(required = false) String region, @RequestParam(required = false) String transport) {
        // 추천 카드 생성 (점수/정렬 + 상위 K LLM 반영)
        return placeRecommendService.recommendCards(style, budget, region, transport);
    }

}
