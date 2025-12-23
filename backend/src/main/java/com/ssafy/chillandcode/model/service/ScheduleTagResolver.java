package com.ssafy.chillandcode.model.service;

import java.util.List;
import java.util.Optional;

import com.ssafy.chillandcode.model.dto.schedule.Schedule;
import com.ssafy.chillandcode.model.dto.schedule.Schedule.ScheduleType;
import com.ssafy.chillandcode.model.dto.schedule.Schedule.Tag;

/**
 * 일정의 의미를 해석하여 시스템 자동 태그(autoTag)를 결정하는 Resolver
 *
 * 핵심 역할: - 일정 등록/수정 시 autoTag를 계산한다 - 추천/분석/BusyDayEvaluator에서 공통으로 신뢰하는 Tag를
 * 생성한다
 */
public class ScheduleTagResolver {
	
	private static final List<String> HIGH_PRIORITY_KEYWORDS = List.of("병원", "면접", "발표", "회의", "미팅", "시험", "상담");
	private static final List<String> PERSONAL_FLEX_KEYWORDS = List.of("점심", "저녁", "식사", "약속", "친구", "커피");
	
	public Tag resolveAutoTag(Schedule schedule) {
		// 1. 제목 기반 태깅
		Optional<Tag> titleBasedTag = extractTagFromTitle(schedule.getTitle());
		
		// 2. fallback: 제목이 애매하면 타입 기반 기본 태그
		Tag baseTag;
		
		if(titleBasedTag.isPresent()) {
			baseTag = titleBasedTag.get();
		} else {
			baseTag = fallbackByScheduleType(schedule.getScheduleType());
		}
		
		// 3. TYPE + TAG 조합 해석 (의미 정규화)
		return normalize(schedule.getScheduleType(), baseTag);
	}
	
	// 제목 기반 태깅
	private Optional<Tag> extractTagFromTitle(String title) {
		
		if(title == null || title.isBlank()) {
			return Optional.empty();
		}
		
		String normalized = title.toLowerCase();
		
		for(String keyword : HIGH_PRIORITY_KEYWORDS) {
			 if (normalized.contains(keyword)) {
	                return Optional.of(Tag.HIGH_PRIORITY);
	            }
		}
		
        for (String keyword : PERSONAL_FLEX_KEYWORDS) {
            if (normalized.contains(keyword)) {
                return Optional.of(Tag.PERSONAL_FLEX);
            }
        }
        
		return Optional.empty();
	}
	
	// 타입 기반 fallback 태깅
	private Tag fallbackByScheduleType(ScheduleType scheduleType) {
		
		 return switch (scheduleType) {
	        case WORK, WORKATION -> Tag.REMOTE_POSSIBLE;
	        case PERSONAL -> Tag.PERSONAL_FLEX;
	    };
	}
	
	// TYPE + TAG 조합 정규화: 새로운 태그를 만들지 않고 의미적으로 이상한 부분 정리
	private Tag normalize(ScheduleType scheduleType, Tag tag) {
		
		// HIGH_PRIORITY는 타입 불문하고 최우선
        if (tag == Tag.HIGH_PRIORITY) {
            return Tag.HIGH_PRIORITY;
        }
        
        // PERSONAL + PERSONAL_FLEX → 조건부 가능
        if (scheduleType == ScheduleType.PERSONAL && tag == Tag.PERSONAL_FLEX) {
            return Tag.PERSONAL_FLEX;
        }
        
        // WORK / WORKATION + REMOTE_POSSIBLE → 기본 가능
        if ((scheduleType == ScheduleType.WORK || scheduleType == ScheduleType.WORKATION)
                && tag == Tag.REMOTE_POSSIBLE) {
            return Tag.REMOTE_POSSIBLE;
        }
		
        // 그 외는 그대로 유지
		return tag;
	}
}
