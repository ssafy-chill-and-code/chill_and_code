package com.ssafy.chillandcode.recommend.place;

import java.util.List;

public class ReasonTextBuilder {

    public static String build(String style, String budget, String transport, List<String> tags) {

        StringBuilder sb = new StringBuilder();

        // style
        if (style != null && !style.isBlank()) {
            sb.append(style).append(" 스타일에 맞는 장소이며 ");
        }

        // budget
        if (budget != null) {
            sb.append(budgetToText(budget)).append(" 예산에 적합합니다");
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
