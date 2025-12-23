package com.ssafy.chillandcode.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * HashtagExtractor는 게시글에서 해시태그를 추출하는 유틸리티 클래스입니다.
 */
public class HashtagExtractor {
    
    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#[^\\s#]+");
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 제목과 내용에서 사용자가 직접 작성한 해시태그만 추출하고 JSON 문자열로 반환
     */
    public static String extractAndSerialize(String title, String content) {
        Set<String> tags = new HashSet<>();
        
        // 제목에서 해시태그 추출
        if (title != null && !title.trim().isEmpty()) {
            Matcher titleMatcher = HASHTAG_PATTERN.matcher(title);
            while (titleMatcher.find()) {
                tags.add(titleMatcher.group());
            }
        }
        
        // 내용에서 해시태그 추출
        if (content != null && !content.trim().isEmpty()) {
            Matcher contentMatcher = HASHTAG_PATTERN.matcher(content);
            while (contentMatcher.find()) {
                tags.add(contentMatcher.group());
            }
        }
        
        // JSON 문자열로 변환
        try {
            return objectMapper.writeValueAsString(new ArrayList<>(tags));
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
    
    /**
     * JSON 문자열에서 해시태그 리스트로 역직렬화
     */
    public static List<String> deserialize(String tagsJson) {
        if (tagsJson == null || tagsJson.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            return objectMapper.readValue(tagsJson, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }
}

