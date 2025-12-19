
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

        // 최종 점수 = 각 점수의 합
        return workspaceScore + natureScore + activityScore;
    }
}
