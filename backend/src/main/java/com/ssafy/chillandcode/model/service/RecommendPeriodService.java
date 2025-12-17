package com.ssafy.chillandcode.model.service;

import com.ssafy.chillcode.recommend.dto.RecommendPeriodRequest;
import com.ssafy.chillcode.recommend.dto.RecommendPeriodResponse;

public interface RecommendPeriodService {
	
	public RecommendPeriodResponse recommendPeriod(long userId, RecommendPeriodRequest req);
	
}
