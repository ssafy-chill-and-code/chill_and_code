package com.ssafy.chillandcode.recommend.place;

/**
 * style 값에 따라
 * 점수 계산에 사용할 가중치(WeightStrategy)를 선택하는 클래스
 */
public class StyleWeightStrategy {

    // trend 보조 가중치 기본값
    // - trend_score는 0~100 범위로 정규화된 값을 전제로 사용한다.
    // - 데이터 분포/정렬 결과에 따라 조정 가능하도록 기본값(default)만 제공한다.
    // - 스케일 변경 시에는 이 Wt 또는 정규화 방식만 조정한다.
    private static final double DEFAULT_TREND_WEIGHT = 0.02; // default Wt

    // style 입력값을 받아 가중치 묶음을 반환
    // workspace_count가 0~513 범위로 매우 크므로 가중치를 대폭 축소
    // nature/activity_score가 0~0.5 범위로 작지만 20배 스케일업되므로 가중치 증대
    public static WeightStrategy byStyle(String style) {

        // 카페·업무 중심 (workspace 최소화, nature/activity 균형)
        if ("CAFE".equals(style)) {
            return new WeightStrategy(0.15, 0.4, 0.45, DEFAULT_TREND_WEIGHT);

        // 자연·힐링 중심 (workspace 극소화, nature 극대화)
        } else if ("NATURE".equals(style)) {
            return new WeightStrategy(0.05, 0.7, 0.25, DEFAULT_TREND_WEIGHT);

        // 활동·관광 중심 (workspace 최소화, activity 극대화)
        } else {
            return new WeightStrategy(0.1, 0.25, 0.65, DEFAULT_TREND_WEIGHT);
        }
    }
}
