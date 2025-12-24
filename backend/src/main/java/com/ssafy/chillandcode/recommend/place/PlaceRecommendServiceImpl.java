package com.ssafy.chillandcode.recommend.place;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 장소 추천 서비스 구현.
 *
 * 흐름
 * - DB에서 후보 장소를 조회하고, 스타일 가중치로 점수를 계산/정렬한다.
 * - 상위 K(최대 20) 장소를 llm-service에 전달해 태그/추천이유(reasonText)를 생성한다.
 * - LLM 응답이 없거나 일부 누락된 경우 로컬 규칙으로 표현을 보강한다(fallback).
 */
@Service
public class PlaceRecommendServiceImpl implements PlaceRecommendService {

	private static final Logger log = LoggerFactory.getLogger(PlaceRecommendServiceImpl.class);
	private static final String NO_IMAGE = "NO_IMAGE";

    // 장소 피처 조회용 저장소
    private final PlaceRepository placeRepository;
    // 태그/추천이유 생성을 담당하는 LLM 클라이언트
    private final LlmClient llmClient;

    public PlaceRecommendServiceImpl(PlaceRepository placeRepository, LlmClient llmClient) {
        this.placeRepository = placeRepository;
        this.llmClient = llmClient;
    }

    /**
     * 점수 계산/정렬은 내부에서, 표현(태그/이유)은 LLM에 위임해 카드 리스트를 생성한다.
     * 다중 지역 선택 시 지역별로 균등하게 분배하여 추천한다.
     */
    @Override
    public List<PlaceRecommendCard> recommendCards(String style, String budget, String region, String transport) {

		// 1) budget → priceLevel (명확한 구분)
		int minPrice, maxPrice;
		if ("LOW".equalsIgnoreCase(budget)) {
			minPrice = 1;
			maxPrice = 2;
		} else if ("MID".equalsIgnoreCase(budget)) {
			minPrice = 3;
			maxPrice = 3;
		} else {
			minPrice = 4;
			maxPrice = 5;
		}

        // 2) region 파싱 (콤마로 구분된 지역 리스트)
        List<String> selectedRegions = new ArrayList<>();
        if (region != null && !region.trim().isEmpty()) {
            for (String r : region.split(",")) {
                String trimmed = r.trim();
                if (!trimmed.isEmpty()) {
                    selectedRegions.add(trimmed);
                }
            }
        }

        // 3) DB 조회 (Projection) - 다중 지역 지원
        List<PlaceFeatureView> views = placeRepository.findByRegionAndBudget(
            region, 
            selectedRegions.isEmpty() ? null : selectedRegions, 
            minPrice, 
            maxPrice
        );

        // 4) 가중치
        WeightStrategy weight = StyleWeightStrategy.byStyle(style);

        log.info("🔍 장소 추천 시작 - style: {}, budget: {}, region: {}", style, budget, region);
        log.info("📊 DB에서 조회된 장소 수: {}", views.size());

        // 5) 점수 계산 및 상위 6개 선택 (예산 범위 내에서만!)
        List<ScoredView> finalSelection = views.stream()
                .map(v -> new ScoredView(v, toFeature(v), weight))
                .sorted(Comparator.comparingDouble(ScoredView::score).reversed())
                .limit(6)
                .collect(Collectors.toList());

        log.info("✅ 선택된 장소 (예산 범위: {}-{}, 점수 순 상위 6개):", minPrice, maxPrice);
        for (int i = 0; i < finalSelection.size(); i++) {
            ScoredView sv = finalSelection.get(i);
            log.info("  {}. {} ({}) [{}] - 점수: {}, price_level: {} [workspace:{}, nature:{}, activity:{}]", 
                i+1, sv.view().getName(), sv.view().getSido(), sv.view().getPlaceType(),
                String.format("%.2f", sv.score()),
                sv.view().getPriceLevel(),
                sv.view().getWorkspaceCount(), 
                sv.view().getNatureScore(), 
                sv.view().getActivityScore());
        }

        // 6) LLM 전달
        List<LlmPlaceInput> inputs = finalSelection.stream().map(sv ->
                new LlmPlaceInput(
                        sv.view().getPlaceId(),
                        sv.view().getName(),
                        sv.score(),
                        sv.view().getNatureScore(),
                        sv.view().getActivityScore(),
                        sv.view().getWorkspaceCount(),
                        sv.view().getTrendScore()
                )
        ).collect(Collectors.toList());

        // llm-service 호출 → placeId별 tags/reasonText 매핑 결과 수신
        Map<Long, LlmResult> llmResultMap = llmClient.request(style, budget, transport, inputs);

        // 7) 카드 변환 (LLM 결과 있으면 반영, 없으면 로컬 규칙)
        return finalSelection.stream()
                .map(sv -> toCard(sv.view(), sv.score(), style, budget, transport, llmResultMap))
                .collect(Collectors.toList());
    }

    /** Projection → 점수 엔진용 Feature로 변환 */
    private PlaceFeature toFeature(PlaceFeatureView v) {
		return new PlaceFeature(
                v.getName(),
                v.getWorkspaceCount(),
                v.getNatureScore(),
                v.getActivityScore(),
                v.getPriceLevel(),
                v.getSido(),
                v.getPlaceId(),
                v.getTrendScore()
        );
	}

    /** Feature/점수 → UI 카드 변환(LLM 결과 적용, 없으면 로컬 규칙) */
    private PlaceRecommendCard toCard(PlaceFeatureView v, double score, String style, String budget, String transport, Map<Long, LlmResult> llmResultMap) {

		String imageUrl = safeImageUrl(v.getImageUrl());

		LlmResult llm = llmResultMap.get(v.getPlaceId());

            // LLM 태그가 있으면 적용, 없으면 로컬 태그 생성
            List<String> tags = (llm != null) ? llm.getTags() : buildTags(v, style, budget);

            // LLM 추천이유가 있으면 적용, 없으면 로컬 규칙으로 생성
            String reasonText = (llm != null) ? llm.getReasonText()
                    : ReasonTextBuilder.build(style, budget, transport, v.getTrendScore());

		return new PlaceRecommendCard(
                v.getPlaceId(),
                v.getName(),
                v.getSido(),
                score,
                v.getTrendScore(), // 프론트 좌상단 % 표기용
                imageUrl,
                tags,
                reasonText
        );
	}

	// ---------- helpers ----------

    /** null/공백 URL 보호 */
    private String safeImageUrl(String url) {
		if (url == null)
			return NO_IMAGE;
		String t = url.trim();
		return t.isEmpty() ? NO_IMAGE : t;
	}

    /** 기본 태그 생성(LLM 부재 시 사용) */
    private List<String> buildTags(PlaceFeatureView v, String style, String budget) {
		List<String> tags = new ArrayList<>();
		if (style != null && !style.isBlank())
			tags.add("#" + style);
		if ("LOW".equalsIgnoreCase(budget))
			tags.add("#가성비");
		if ("MID".equalsIgnoreCase(budget))
			tags.add("#적당한가격");
		if ("HIGH".equalsIgnoreCase(budget))
			tags.add("#프리미엄");
		if (v.getWorkspaceCount() > 0)
			tags.add("#작업가능");
		return tags;
	}

    /** 뷰 + 점수 계산을 묶는 내부 타입 */
    private static class ScoredView {

		private final PlaceFeatureView view;
		private final PlaceFeature feature;
		private final WeightStrategy weight;

		private ScoredView(PlaceFeatureView view, PlaceFeature feature, WeightStrategy weight) {
			this.view = view;
			this.feature = feature;
			this.weight = weight;
		}

		public PlaceFeatureView view() {
			return view;
		}

		public double score() {
			return PlaceScoreEngine.calculate(feature, weight);
		}
	}

}
