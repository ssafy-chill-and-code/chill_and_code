package com.ssafy.chillandcode.recommend.place;

/**
 * ScoreEngine 입력값 역할
 */
public class PlaceFeature {

	// 식별/표시용 
	private Long placeId;
	private String name;
	private String sido;

	// 계산 대상
	private int workspaceCount;
	private double natureScore;
	private double activityScore;
    // 트렌드 점수(0~100 정규화, 배치 산출)
    private double trendScore;

	// 필터용
	private int priceLevel;

	public PlaceFeature(String name, int workspaceCount, double natureScore, double activityScore, int priceLevel,
			String sido, Long placeId, double trendScore) {
		this.name = name;
		this.workspaceCount = workspaceCount;
		this.natureScore = natureScore;
		this.activityScore = activityScore;
		this.priceLevel = priceLevel;
		this.sido = sido;
		this.placeId = placeId;
        this.trendScore = trendScore;
	}

	public String getName() {
		return name;
	}

	public int getWorkspaceCount() {
		return workspaceCount;
	}

	public double getNatureScore() {
		return natureScore;
	}

	public double getActivityScore() {
		return activityScore;
	}

    public double getTrendScore() {
        return trendScore;
    }

	public int getPriceLevel() {
		return priceLevel;
	}

	public String getSido() {
		return sido;
	}

	public Long getPlaceId() {
		return placeId;
	}

}
