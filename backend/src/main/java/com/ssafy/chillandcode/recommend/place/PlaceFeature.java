package com.ssafy.chillandcode.recommend.place;

public class PlaceFeature {

	private String name;

	/**
	 * 계산 대상
	 */
	private int workspaceCount;
	private double natureScore;
	private double activityScore;

	/**
	 * 판단 대상(필터용)
	 */
	private int priceLevel;
	private String sido;

	public PlaceFeature(String name, int workspaceCount, double natureScore, double activityScore, int priceLevel,
			String sido) {
		this.name = name;
		this.workspaceCount = workspaceCount;
		this.natureScore = natureScore;
		this.activityScore = activityScore;
		this.priceLevel = priceLevel;
		this.sido = sido;
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

	public int getPriceLevel() {
		return priceLevel;
	}

	public String getSido() {
		return sido;
	}

}
