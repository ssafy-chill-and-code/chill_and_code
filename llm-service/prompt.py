from typing import Optional


def build_prompt(style: str, budget: str, transport: Optional[str], places: list) -> str:

    lines = []

    # 역할 및 일반 지침
    lines.append("너는 추천 이유를 생성하는 AI다.")
    lines.append("이미 점수 계산과 정렬은 완료되었다.")
    lines.append("순서를 변경하거나 점수를 다시 계산하지 마라.")
    lines.append("")
    lines.append("반드시 아래 JSON 형식으로만 출력하라.")
    lines.append("JSON 이외의 설명, 문장, 마크다운 출력 금지.")
    lines.append("")
    lines.append(
        """
출력 형식:
{
  "recommendations": [
    {
      "placeId": number,
      "tags": [string],
      "reasonText": string
    }
  ]
}
"""
    )

    # 조건(하드 제약)
    lines.append("[조건]")
    lines.append("- recommendations 배열의 순서는 입력 장소 순서와 동일")
    lines.append("- placeId는 입력으로 주어진 값만 사용")
    lines.append("- tags는 3개 이내")
    # 문장 수 제한 제거: reasonText 길이는 자유
    if transport:
        # 자연스럽게 운송 수단 맥락을 녹여 쓰되, 부자연스러운 반복/정형 문구는 피함
        lines.append("- transport가 주어지면 접근성/이동 편의 맥락을 reasonText에 자연스럽게 반영")
    lines.append("")

    # 스타일링 가이드(권고)
    lines.append("[작성 가이드]")
    lines.append("- 한국어로 자연스럽게 작성하되, 문장 길이와 어조(설명/감성/설득)를 다양화")
    lines.append("- 동일한 템플릿 문구 반복 금지(예: '~에 맞는 장소이며 ~에 적합합니다' 금지)")
    lines.append("- 장소의 특성(nature/activity/workspace)을 구체적으로 드러내고, 사용자 style/budget 맥락을 연결")
    lines.append("- 문장 끝맺음과 표현을 반복하지 말고 적절히 변주")
    lines.append("")
    lines.append("[trendScore 활용 가이드]")
    lines.append("- trendScore(0~100)는 최근 관심도 신호이며, 현재성/트렌드를 표현하는 보조 요소입니다")
    lines.append("- trendScore가 높을 때(70 이상)만 자연스럽게 언급하고, 낮으면 굳이 언급하지 않습니다")
    lines.append("- 숫자, 점수, 순위, 랭킹 같은 표현은 사용하지 않고 의미 기반 표현만 사용합니다")
    lines.append("- 금지 표현: '핫', '핫 플레이스', '인기 급증', '급증', '인기 순위', '트렌디한' 등 과장된 표현")
    lines.append("- 허용 표현: '최근 관심이 높아지는', '관심이 늘어나는 추세', '점차 주목받고 있는' 등 완화된 표현")
    lines.append("- style별로 trend 표현 강도와 방식이 달라집니다:")
    lines.append("  * CAFE: trend 언급을 최소화 (가능하면 생략)")
    lines.append("  * NATURE: trend가 높을 때만 자연스럽게 언급")
    lines.append("  * ACTIVITY: trend 표현을 완화된 톤으로만 사용 (보조적인 느낌만)")
    lines.append("- 문장 구조 규칙:")
    lines.append("  1. 첫 문장은 반드시 style 중심 설명 (ACTIVITY/체험/활동 중심)")
    lines.append("  2. trend 관련 문장은 보조 문장으로 뒤에 배치")
    lines.append("  3. trend 문장이 문장의 핵심이 되지 않도록 할 것")
    lines.append("- trend 표현은 문구의 톤/수식어/강조 표현으로만 반영하며, 기존 style 중심 설명은 유지합니다")
    lines.append("")

    # 사용자 조건
    lines.append(f"[사용자 조건]")
    lines.append(f"- style: {style}")
    lines.append(f"- budget: {budget}")
    if transport:
        lines.append(f"- transport: {transport}")

    lines.append("")

    # 장소 목록
    lines.append("[장소 목록]")
    for p in places:
        lines.append(
            f"- placeId: {p.placeId}, "
            f"name: {p.name}, "
            f"score: {p.score}, "
            f"nature: {p.natureScore}, "
            f"activity: {p.activityScore}, "
            f"workspace: {p.workspaceCount}, "
            f"trend: {p.trendScore}"
        )

    return "\n".join(lines)
