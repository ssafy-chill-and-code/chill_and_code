package com.ssafy.chillandcode.recommend.place;

import java.util.List;

/**
 * 장소 추천 카드 정보를 담는 DTO입니다.
 */
public class PlaceRecommendCard {

	private Long placeId;
	private String name;
	private String region;
	private double score;
	private String imageUrl;
	private List<String> tags;
	private String reasonText;

	public PlaceRecommendCard(Long placeId, String name, String region, double score, String imageUrl,
			List<String> tags, String reasonText) {
		super();
		this.placeId = placeId;
		this.name = name;
		this.region = region;
		this.score = score;
		this.imageUrl = imageUrl;
		this.tags = tags;
		this.reasonText = reasonText;
	}

	public Long getPlaceId() {
		return placeId;
	}

	public String getName() {
		return name;
	}

	public String getRegion() {
		return region;
	}

	public double getScore() {
		return score;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public List<String> getTags() {
		return tags;
	}

	public String getReasonText() {
		return reasonText;
	}

}
