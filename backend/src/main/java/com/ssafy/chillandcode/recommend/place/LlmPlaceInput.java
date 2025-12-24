package com.ssafy.chillandcode.recommend.place;

/**
 * llm-service로 전달할 장소 입력 DTO
 */
public class LlmPlaceInput {
    private Long placeId;
    private String name;
    private double score;
    private double natureScore;
    private double activityScore;
    private int workspaceCount;
    private double trendScore;

    public LlmPlaceInput(Long placeId, String name, double score, double natureScore, double activityScore, int workspaceCount, double trendScore) {
        this.placeId = placeId;
        this.name = name;
        this.score = score;
        this.natureScore = natureScore;
        this.activityScore = activityScore;
        this.workspaceCount = workspaceCount;
        this.trendScore = trendScore;
    }

    public Long getPlaceId() { return placeId; }
    public String getName() { return name; }
    public double getScore() { return score; }
    public double getNatureScore() { return natureScore; }
    public double getActivityScore() { return activityScore; }
    public int getWorkspaceCount() { return workspaceCount; }
    public double getTrendScore() { return trendScore; }
}

