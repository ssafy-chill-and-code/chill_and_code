package com.ssafy.chillandcode.recommend.place;

/**
 * style 값에 따라
 * 점수 계산에 사용할 가중치(WeightStrategy)를 선택하는 클래스
 */
public class StyleWeightStrategy {

    // style 입력값을 받아 가중치 묶음을 반환
    // nature/activity_score가 0.0 또는 0.5로만 존재하여 차별화가 어려움
    // → workspace_count와 trend_score의 비중을 높여 차별화 (선택과 집중)
    public static WeightStrategy byStyle(String style) {

        // 카페·업무 중심 (workspace 중심, trend로 차별화)
        if ("CAFE".equals(style)) {
            return new WeightStrategy(0.80, 0.15, 0.15, 0.05);

        // 자연·힐링 중심 (nature 중심, trend 보조)
        } else if ("NATURE".equals(style)) {
            return new WeightStrategy(0.02, 0.75, 0.23, 0.03);

        // 활동·관광 중심 (activity 중심, trend 반영)
        } else {
            return new WeightStrategy(0.03, 0.22, 0.75, 0.08);
        }
    }
}
