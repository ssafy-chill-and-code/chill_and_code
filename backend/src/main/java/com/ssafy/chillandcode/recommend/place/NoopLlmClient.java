package com.ssafy.chillandcode.recommend.place;

import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * Minimal stub implementation of LlmClient to allow application startup
 * when no concrete LLM integration is configured yet.
 */
@Service
public class NoopLlmClient implements LlmClient {

    @Override
    public Map<Long, LlmResult> requestLlmResults(String style, String budget) {
        // Return empty results so downstream service falls back to local tagging/reason logic
        return Map.of();
    }
}

