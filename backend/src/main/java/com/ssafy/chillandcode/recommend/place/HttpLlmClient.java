package com.ssafy.chillandcode.recommend.place;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * llm-service HTTP 클라이언트(@Primary).
 * - userContext + 상위 K 장소 리스트를 FastAPI llm-service에 POST로 전달한다.
 * - 응답의 recommendations를 placeId 기준으로 매핑해 반환하며, 실패 시 빈 맵을 돌려
 *   서비스 레이어가 로컬 규칙으로 fallback 하도록 한다.
 */
@Primary
@Service
public class HttpLlmClient implements LlmClient {

    private static final Logger log = LoggerFactory.getLogger(HttpLlmClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public HttpLlmClient(@Value("${llm.base-url:http://localhost:8000}") String baseUrl,
                         @Value("${llm.timeout-ms:5000}") int timeoutMs) {
        this.baseUrl = baseUrl;
        SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(timeoutMs);
        f.setReadTimeout(timeoutMs);
        this.restTemplate = new RestTemplate(f);
    }

    @Override
    /** llm-service에 추천 표현 생성 요청 */
    public Map<Long, LlmResult> request(String style, String budget, String transport, List<LlmPlaceInput> places) {
        try {
            String url = baseUrl + "/llm/recommend";

            Map<String, Object> body = new HashMap<>();
            Map<String, Object> userContext = new HashMap<>();
            userContext.put("style", style);
            userContext.put("budget", budget);
            if (transport != null && !transport.isBlank()) {
                userContext.put("transport", transport);
            }
            body.put("userContext", userContext);

            // places → 직렬화 가능한 Map 리스트로 변환
            List<Map<String, Object>> placeList = places.stream().map(p -> {
                Map<String, Object> m = new HashMap<>();
                m.put("placeId", p.getPlaceId());
                m.put("name", p.getName());
                m.put("score", p.getScore());
                m.put("natureScore", p.getNatureScore());
                m.put("activityScore", p.getActivityScore());
                m.put("workspaceCount", p.getWorkspaceCount());
                return m;
            }).collect(java.util.stream.Collectors.toList());
            body.put("places", placeList);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            // FastAPI 응답 스키마: { status, llmResult: { recommendations: [...] } }
            ResponseEntity<Map> res = restTemplate.postForEntity(url, entity, Map.class);
            Object llmResultObj = res.getBody().get("llmResult");
            if (!(llmResultObj instanceof Map)) {
                return Map.of();
            }
            Map<String, Object> llmResult = (Map<String, Object>) llmResultObj;
            Object recsObj = llmResult.get("recommendations");
            if (!(recsObj instanceof List)) {
                return Map.of();
            }

            Map<Long, LlmResult> out = new HashMap<>();
            for (Object item : (List<?>) recsObj) {
                if (!(item instanceof Map)) continue;
                Map<?, ?> r = (Map<?, ?>) item;
                Object idObj = r.get("placeId");
                if (idObj == null) continue;
                Long placeId = Long.valueOf(String.valueOf(idObj));

                @SuppressWarnings("unchecked")
                List<String> tags = (List<String>) r.get("tags");
                String reasonText = r.get("reasonText") != null ? String.valueOf(r.get("reasonText")) : null;
                if (reasonText == null) continue;
                out.put(placeId, new LlmResult(placeId, tags, reasonText));
            }
            return out;
        } catch (Exception e) {
            // 네트워크 오류/타임아웃/파싱 실패 등은 빈 맵 반환하여 서비스 레이어가 fallback 하도록 함
            log.warn("LLM call failed, falling back. cause={}", e.toString());
            return Map.of();
        }
    }
}
