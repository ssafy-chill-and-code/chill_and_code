
package com.ssafy.chillandcode.recommend.place;

/**
 * 장소 하나의 추천 점수를 계산한다
 *
 * @param feature 장소의 수치 정보 (도메인)
 * @param weight  style에 따라 선택된 가중치
 * @return 계산된 추천 점수
 */
public class PlaceScoreEngine {

    public static double calculate(PlaceFeature feature, WeightStrategy weight) {

        // workspace 점수 반영
        double workspaceScore =
                feature.getWorkspaceCount() * weight.getWorkspace();

        // nature 점수 반영
        double natureScore =
                feature.getNatureScore() * weight.getNature();

        // activity 점수 반영
        double activityScore =
                feature.getActivityScore() * weight.getActivity();

        // trend 점수 반영 (보조 항)
        // - trend_score는 0~100 범위로 정규화된 값을 전제로 사용한다.
        // - 스케일 변경 시에는 Wt(StyleWeightStrategy) 또는 정규화 방식만 조정한다.
        double trendScore =
                feature.getTrendScore() * weight.getTrend();

        // 최종 점수 = 각 점수의 합
        return workspaceScore + natureScore + activityScore + trendScore;
    }
}
