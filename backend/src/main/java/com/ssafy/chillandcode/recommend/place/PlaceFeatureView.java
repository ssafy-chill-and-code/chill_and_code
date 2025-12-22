package com.ssafy.chillandcode.recommend.place;

/**
 * DB 조회 전용 DTO (Projection)
 * - 계산 + 카드 조립에 필요한 최소 정보만 포함
 */
public class PlaceFeatureView {

    // 식별/표시
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

    // 이미지 URL
    private String imageUrl;

    public Long getPlaceId() { return placeId; }
    public String getName() { return name; }
    public String getSido() { return sido; }
    public int getWorkspaceCount() { return workspaceCount; }
    public double getNatureScore() { return natureScore; }
    public double getActivityScore() { return activityScore; }
    public double getTrendScore() { return trendScore; }
    public int getPriceLevel() { return priceLevel; }
    public String getImageUrl() { return imageUrl; }

    public void setPlaceId(Long placeId) { this.placeId = placeId; }
    public void setName(String name) { this.name = name; }
    public void setSido(String sido) { this.sido = sido; }
    public void setWorkspaceCount(int workspaceCount) { this.workspaceCount = workspaceCount; }
    public void setNatureScore(double natureScore) { this.natureScore = natureScore; }
    public void setActivityScore(double activityScore) { this.activityScore = activityScore; }
    public void setTrendScore(double trendScore) { this.trendScore = trendScore; }
    public void setPriceLevel(int priceLevel) { this.priceLevel = priceLevel; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
