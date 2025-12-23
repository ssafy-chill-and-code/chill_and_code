package com.ssafy.chillandcode.model.dao.review;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.chillandcode.model.dto.review.PlaceReview;
import com.ssafy.chillandcode.model.dto.review.PlaceReviewResponse;

/**
 * 장소 리뷰 정보에 대한 DB 접근을 담당
 */
@Mapper
public interface ReviewDao {

    /**
     * 장소명과 지역으로 리뷰 목록 조회
     */
    List<PlaceReviewResponse> selectByPlaceName(Map<String, Object> params);

    /**
     * 장소명과 지역으로 리뷰 개수 조회
     */
    int countByPlaceName(Map<String, Object> params);

    /**
     * 장소명과 지역으로 평균 별점 조회
     */
    Double selectAverageRating(Map<String, Object> params);

    /**
     * 장소 리뷰 작성
     */
    int insert(PlaceReview placeReview);
}

