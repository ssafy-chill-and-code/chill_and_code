package com.ssafy.chillandcode.recommend.place;

import java.util.List;
import java.util.Map;

/**
 * LLM 호출 전용 컴포넌트
 */
public interface LlmClient {
    /**
     * 사용자 조건과 정렬 완료된 상위 장소 목록을 전달해 태그/이유를 받아온다.
     */
    Map<Long, LlmResult> request(String style, String budget, String transport, List<LlmPlaceInput> places);
}
