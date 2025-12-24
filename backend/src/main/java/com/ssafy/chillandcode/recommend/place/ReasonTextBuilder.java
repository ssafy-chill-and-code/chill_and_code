package com.ssafy.chillandcode.recommend.place;

public class ReasonTextBuilder {

    public static String build(String style, String budget, String transport) {
        return build(style, budget, transport, 0.0);
    }

    public static String build(String style, String budget, String transport, double trendScore) {

        StringBuilder sb = new StringBuilder();

        // style
        if (style != null && !style.isBlank()) {
            sb.append(style).append(" 스타일에 맞는 장소이며 ");
        }

        // budget
        if (budget != null) {
            sb.append(budgetToText(budget)).append(" 예산에 적합합니다");
        }

        // trend 맥락 (trendScore >= 70일 때만 선택적으로 추가)
        if (trendScore >= 70) {
            String styleUpper = style != null ? style.toUpperCase() : "";
            // style별 trend 표현 강도: CAFE는 최소화, ACTIVITY는 완화된 톤
            if (!"CAFE".equals(styleUpper)) {
                if ("ACTIVITY".equals(styleUpper)) {
                    sb.append(". 최근 관심이 높아지는 추세예요");
                } else {
                    sb.append(". 최근 관심이 높아지고 있어요");
                }
            }
        }

        // transport (설명 전용)
        if (transport != null && !transport.isBlank()) {
            sb.append(". ").append(transport).append(" 이동을 고려해 추천했어요");
        }

        return sb.toString();
    }

    private static String budgetToText(String budget) {
        return switch (budget.toUpperCase()) {
            case "LOW"  -> "가성비";
            case "MID"  -> "적당한";
            case "HIGH" -> "프리미엄";
            default     -> "";
        };
    }
}
