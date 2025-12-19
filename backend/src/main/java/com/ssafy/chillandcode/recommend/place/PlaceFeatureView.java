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

    // 필터용
    private int priceLevel;

    // 이미지 판단용
    private String imageStatus; // REAL / NO_IMAGE
    private String imageUrl;

    public Long getPlaceId() { return placeId; }
    public String getName() { return name; }
    public String getSido() { return sido; }
    public int getWorkspaceCount() { return workspaceCount; }
    public double getNatureScore() { return natureScore; }
    public double getActivityScore() { return activityScore; }
    public int getPriceLevel() { return priceLevel; }
    public String getImageStatus() { return imageStatus; }
    public String getImageUrl() { return imageUrl; }
}
