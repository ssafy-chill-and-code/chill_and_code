package com.ssafy.chillandcode.recommend.place;

/**
 * style 값에 따라
 * 점수 계산에 사용할 가중치(WeightStrategy)를 선택하는 클래스
 */
public class StyleWeightStrategy {

    // style 입력값을 받아 가중치 묶음을 반환
    public static WeightStrategy byStyle(String style) {

        // 카페·업무 중심
        if ("CAFE".equals(style)) {
            return new WeightStrategy(0.6, 0.3, 0.1);

        // 자연·힐링 중심
        } else if ("NATURE".equals(style)) {
            return new WeightStrategy(0.2, 0.6, 0.2);

        // 활동·관광 중심 (기본)
        } else {
            return new WeightStrategy(0.3, 0.3, 0.4);
        }
    }
}
