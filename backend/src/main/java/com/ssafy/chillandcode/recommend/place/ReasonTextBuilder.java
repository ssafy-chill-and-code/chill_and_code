package com.ssafy.chillandcode.recommend.place;

import java.util.List;

public class ReasonTextBuilder {

	public static String build(String style, String budget, String transport, List<String> tags) {
		return style + "스타일 + " + budget + "예산 기준 추천";
	}
}
