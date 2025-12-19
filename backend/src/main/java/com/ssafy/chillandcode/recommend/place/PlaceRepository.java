package com.ssafy.chillandcode.recommend.place;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * 장소 추천 후보를 조회하는 Repository
 * (region, budget 필터 전용)
 */
@Mapper
public interface PlaceRepository {

	List<PlaceFeatureView> findByRegionAndBudget(
			@Param("sido") String sido,
			@Param("minPriceLevel") int minPriceLevel,
			@Param("maxPriceLevel") int maxPriceLevel
	);
	
}
