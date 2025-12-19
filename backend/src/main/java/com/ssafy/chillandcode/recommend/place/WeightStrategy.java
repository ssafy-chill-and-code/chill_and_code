package com.ssafy.chillandcode.recommend.place;

/**
 * 점수 계산에 사용되는 가중치 데이터를 담는 클래스
 * (계산 로직 없음, 불변 객체)
 */
public class WeightStrategy {

    // 각 feature에 대한 가중치 값
    private final double workspace;
    private final double nature;
    private final double activity;

    // 생성 시 가중치 값 확정
    public WeightStrategy(double workspace, double nature, double activity) {
        this.workspace = workspace;
        this.nature = nature;
        this.activity = activity;
    }

    // 가중치 조회용 getter
    public double getWorkspace() { return workspace; }
    public double getNature() { return nature; }
    public double getActivity() { return activity; }
}
