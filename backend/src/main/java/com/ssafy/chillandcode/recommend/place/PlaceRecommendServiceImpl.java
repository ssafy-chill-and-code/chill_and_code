package com.ssafy.chillandcode.recommend.place;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class PlaceRecommendServiceImpl implements PlaceRecommendService {

	private static final String NO_IMAGE = "NO_IMAGE";

	private final PlaceRepository placeRepository;

	// 요청 단위로 세팅되는 LLM 결과
	private Map<Long, LlmResult> llmResultMap = Map.of();

	public PlaceRecommendServiceImpl(PlaceRepository placeRepository) {
		this.placeRepository = placeRepository;
	}

	@Override
	public List<PlaceRecommendCard> recommendCards(String style, String budget, String region, String transport,
			Map<Long, LlmResult> llmResultMap) {

		// 1) budget → priceLevel
		int minPrice, maxPrice;
		if ("LOW".equalsIgnoreCase(budget)) {
			minPrice = 1;
			maxPrice = 2;
		} else if ("MID".equalsIgnoreCase(budget)) {
			minPrice = 2;
			maxPrice = 3;
		} else {
			minPrice = 3;
			maxPrice = 5;
		}

		// 2) DB 조회 (Projection)
		List<PlaceFeatureView> views = placeRepository.findByRegionAndBudget(region, minPrice, maxPrice);

		// 3) 가중치
		WeightStrategy weight = StyleWeightStrategy.byStyle(style);

		// 4) 점수 계산 + 카드 변환
		return views.stream().map(v -> new ScoredView(v, toFeature(v), weight))
				.sorted(Comparator.comparingDouble(ScoredView::score).reversed())
				.map(sv -> toCard(sv.view(), sv.score(), style, budget, transport, llmResultMap 
				)).collect(Collectors.toList());
	}

	public void applyLlmResults(Map<Long, LlmResult> llmResultMap) {
		this.llmResultMap = llmResultMap != null ? llmResultMap : Map.of();
	}

	/**
	 * Projection → Engine 전용 Feature
	 */
	private PlaceFeature toFeature(PlaceFeatureView v) {
		return new PlaceFeature(v.getName(), v.getWorkspaceCount(), v.getNatureScore(), v.getActivityScore(),
				v.getPriceLevel(), v.getSido(), v.getPlaceId());
	}

	/**
	 * Feature → UI Card
	 */
	private PlaceRecommendCard toCard(PlaceFeatureView v, double score, String style, String budget, String transport,Map<Long, LlmResult> llmResultMap) {

		String imageUrl = NO_IMAGE.equalsIgnoreCase(v.getImageStatus()) ? NO_IMAGE : safeImageUrl(v.getImageUrl());

		LlmResult llm = llmResultMap.get(v.getPlaceId());

		List<String> tags = (llm != null) ? llm.getTags() : buildTags(v, style, budget);

		String reasonText = (llm != null) ? llm.getReasonText()
				: ReasonTextBuilder.build(style, budget, transport, tags);

		return new PlaceRecommendCard(v.getPlaceId(), v.getName(), v.getSido(), score, imageUrl, tags, reasonText);
	}

	// ---------- helpers ----------

	private String safeImageUrl(String url) {
		if (url == null)
			return NO_IMAGE;
		String t = url.trim();
		return t.isEmpty() ? NO_IMAGE : t;
	}

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
