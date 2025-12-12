package com.ssafy.chillandcode.model.dto.post;

/**
 * RegionRank는 지역별 게시글 수 집계 결과를 담는 DTO 입니다.
 */
public class RegionRank {
    private String region;
    private Long count;

    public RegionRank() {}

    public RegionRank(String region, Long count) {
        this.region = region;
        this.count = count;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

