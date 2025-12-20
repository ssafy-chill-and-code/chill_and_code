package com.ssafy.chillandcode.recommend.place;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 아직 실제 LLM 연동이 설정되지 않았을 때도
 * 애플리케이션이 정상적으로 기동될 수 있도록 하는
 * LlmClient의 최소한의 임시(stub) 구현체입니다.
 */

@Service
@ConditionalOnProperty(value = "llm.enabled", havingValue = "false", matchIfMissing = true)
public class NoopLlmClient implements LlmClient {

    @Override
    public Map<Long, LlmResult> request(String style, String budget, String transport, java.util.List<LlmPlaceInput> places) {
        // 빈 결과를 반환하여 서비스가 로컬 규칙으로 fallback 하도록 함
        return Map.of();
    }
}
