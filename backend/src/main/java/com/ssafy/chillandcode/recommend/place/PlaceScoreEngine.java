
package com.ssafy.chillandcode.recommend.place;

/**
 * 장소 하나의 추천 점수를 계산한다
 *
 * @param feature 장소의 수치 정보 (도메인)
 * @param weight  style에 따라 선택된 가중치
 * @return 계산된 추천 점수
 */
public class PlaceScoreEngine {

    // 정규화 최대값 (모든 점수를 0~10 스케일로 통일)
    private static final double MAX_WORKSPACE_NORMALIZED = 10.0;
    private static final double MAX_NATURE_NORMALIZED = 10.0;
    private static final double MAX_ACTIVITY_NORMALIZED = 10.0;

    public static double calculate(PlaceFeature feature, WeightStrategy weight) {

        // workspace 점수 반영 (정규화: 0~10 스케일)
        double normalizedWorkspace = Math.min(feature.getWorkspaceCount(), MAX_WORKSPACE_NORMALIZED);
        double workspaceScore = normalizedWorkspace * weight.getWorkspace();

        // nature 점수 반영 (정규화: 0~10 스케일)
        double normalizedNature = Math.min(feature.getNatureScore(), MAX_NATURE_NORMALIZED);
        double natureScore = normalizedNature * weight.getNature();

        // activity 점수 반영 (정규화: 0~10 스케일)
        double normalizedActivity = Math.min(feature.getActivityScore(), MAX_ACTIVITY_NORMALIZED);
        double activityScore = normalizedActivity * weight.getActivity();

        // trend 점수 반영 (보조 항)
        // - trend_score는 0~100 범위로 정규화된 값을 전제로 사용한다.
        // - 스케일 변경 시에는 Wt(StyleWeightStrategy) 또는 정규화 방식만 조정한다.
        double trendScore =
                feature.getTrendScore() * weight.getTrend();

        // 최종 점수 = 각 점수의 합
        return workspaceScore + natureScore + activityScore + trendScore;
    }
}
