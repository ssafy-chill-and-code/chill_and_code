package com.ssafy.chillandcode.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ImageExtractor는 텍스트에서 이미지 URL을 추출하는 유틸리티 클래스입니다.
 */
public class ImageExtractor {
    
    // HTML img 태그에서 src 추출: <img src="URL">
    private static final Pattern HTML_IMG_PATTERN = Pattern.compile("<img[^>]*src=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
    
    // Markdown 이미지 문법: ![alt](URL)
    private static final Pattern MARKDOWN_IMG_PATTERN = Pattern.compile("!\\[.*?\\]\\(([^)]+)\\)");
    
    // 직접 이미지 URL: http(s)://...jpg/png/gif/webp/svg
    private static final Pattern DIRECT_URL_PATTERN = Pattern.compile("https?://[^\\s]+\\.(jpg|jpeg|png|gif|webp|svg)", Pattern.CASE_INSENSITIVE);
    
    /**
     * 텍스트에서 첫 번째 이미지 URL을 추출합니다.
     * HTML img 태그, Markdown 이미지, 직접 URL 순으로 검색합니다.
     * 
     * @param text 검색할 텍스트
     * @return 첫 번째 이미지 URL, 없으면 null
     */
    public static String extractFirstImageUrl(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        
        // 1. HTML img 태그 검색
        Matcher htmlMatcher = HTML_IMG_PATTERN.matcher(text);
        if (htmlMatcher.find()) {
            return htmlMatcher.group(1);
        }
        
        // 2. Markdown 이미지 검색
        Matcher markdownMatcher = MARKDOWN_IMG_PATTERN.matcher(text);
        if (markdownMatcher.find()) {
            return markdownMatcher.group(1);
        }
        
        // 3. 직접 URL 검색
        Matcher urlMatcher = DIRECT_URL_PATTERN.matcher(text);
        if (urlMatcher.find()) {
            return urlMatcher.group(0);
        }
        
        return null;
    }
}

