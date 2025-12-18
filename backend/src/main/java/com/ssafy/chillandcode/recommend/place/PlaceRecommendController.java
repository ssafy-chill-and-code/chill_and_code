package com.ssafy.chillandcode.recommend.place;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

/**
 * 장소 추천 API style, budget, region을 받아 추천 장소 목록을 반환한다
 */

@RestController
@RequestMapping("/api")
public class PlaceRecommendController {

	@Autowired
	private PlaceRecommendService placeRecommendService;

	@Operation(summary = "장소 추천", description = "style, budget, region 입력을 받아 점수 기반으로 장소를 추천합니다.")
	@GetMapping("/recommend/places")
	public List<PlaceFeature> recommend(@RequestParam String style, @RequestParam String budget,
			@RequestParam(required = false) String sido) {
		return placeRecommendService.recommend(style, budget, sido);
	}

}
