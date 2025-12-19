package com.ssafy.chillandcode.recommend.place;

import java.util.List;
import java.util.Map;

/**
 * LLM 호출 전용 컴포넌트
 */
public interface LlmClient {
	Map<Long, LlmResult> requestLlmResults(String style, String budget);
}
