package com.ssafy.chillandcode.recommend.place;

import java.util.List;

/**
 * LLM이 생성한 장소 추천 표현 결과를 담는 DTO. 
 * (tags, reasonText만 포함하며 계산/정렬 로직은 관여하지 않는다)
 */

public class LlmResult {

	private Long placeId;
	private List<String> tags;
	private String reasonText;

	public LlmResult(Long placeId, List<String> tags, String reasonText) {
		this.placeId = placeId;
		this.tags = tags;
		this.reasonText = reasonText;
	}

	public Long getPlaceId() {
		return placeId;
	}

	public List<String> getTags() {
		return tags;
	}

	public String getReasonText() {
		return reasonText;
	}

}
