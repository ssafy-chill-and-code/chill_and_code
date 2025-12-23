package com.ssafy.chillandcode.model.service;

import com.ssafy.chillandcode.recommend.dto.RecommendPeriodRequest;
import com.ssafy.chillandcode.recommend.dto.RecommendPeriodResponse;

public interface RecommendPeriodService {
	
	public RecommendPeriodResponse recommendPeriod(long userId, RecommendPeriodRequest req);
	
}
