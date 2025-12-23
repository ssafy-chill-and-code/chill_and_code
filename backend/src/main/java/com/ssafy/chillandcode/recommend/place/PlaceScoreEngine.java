
package com.ssafy.chillandcode.recommend.place;

/**
 * 장소 하나의 추천 점수를 계산한다
 *
 * @param feature 장소의 수치 정보 (도메인)
 * @param weight  style에 따라 선택된 가중치
 * @return 계산된 추천 점수
 */
public class PlaceScoreEngine {

    // 정규화 최대값
    // workspace: 평균값 기준 (WORKSPACE:342, ACCOMMODATION:215, SPOT:58)
    // 최대 400으로 제한하여 차별화 유지
    // nature/activity: 0.5 범위를 10으로 스케일업 (x20 배율)
    private static final double MAX_WORKSPACE_NORMALIZED = 400.0;
    private static final double NATURE_SCALE_FACTOR = 20.0; // 0.5 → 10.0
    private static final double ACTIVITY_SCALE_FACTOR = 20.0; // 0.5 → 10.0

    public static double calculate(PlaceFeature feature, WeightStrategy weight) {

        // workspace 점수 반영 (정규화: 0~50 제한)
        double normalizedWorkspace = Math.min(feature.getWorkspaceCount(), MAX_WORKSPACE_NORMALIZED);
        double workspaceScore = normalizedWorkspace * weight.getWorkspace();

        // nature 점수 반영 (스케일업: 0~0.5 → 0~10)
        double scaledNature = feature.getNatureScore() * NATURE_SCALE_FACTOR;
        double natureScore = scaledNature * weight.getNature();

        // activity 점수 반영 (스케일업: 0~0.5 → 0~10)
        double scaledActivity = feature.getActivityScore() * ACTIVITY_SCALE_FACTOR;
        double activityScore = scaledActivity * weight.getActivity();

        // trend 점수 반영 (보조 항)
        // - trend_score는 0~100 범위로 정규화된 값을 전제로 사용한다.
        // - 스케일 변경 시에는 Wt(StyleWeightStrategy) 또는 정규화 방식만 조정한다.
        double trendScore =
                feature.getTrendScore() * weight.getTrend();

        // 최종 점수 = 각 점수의 합
        return workspaceScore + natureScore + activityScore + trendScore;
    }
}
