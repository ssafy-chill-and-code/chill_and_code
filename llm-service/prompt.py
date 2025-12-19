from typing import Optional


def build_prompt(style: str, budget: str, transport: Optional[str], places: list) -> str:

    lines = []

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

    lines.append("[조건]")
    lines.append("- recommendations 배열의 순서는 입력 장소 순서와 동일")
    lines.append("- placeId는 입력으로 주어진 값만 사용")
    lines.append("- tags는 3개 이내")
    lines.append("- reasonText는 한국어 1~2문장")
    if transport:
        # 더 엄격: reasonText에 transport 문자열을 그대로 포함하도록 강제
        lines.append(
            f"- transport가 주어지면 reasonText에 '{transport}' 문자열을 반드시 1회 이상 그대로 포함(동의어/변형 금지)"
        )
    lines.append("")

    lines.append(f"[사용자 조건]")
    lines.append(f"- style: {style}")
    lines.append(f"- budget: {budget}")
    if transport:
        lines.append(f"- transport: {transport}")

    lines.append("")

    lines.append("[장소 목록]")
    for p in places:
        lines.append(
            f"- placeId: {p.placeId}, "
            f"name: {p.name}, "
            f"score: {p.score}, "
            f"nature: {p.natureScore}, "
            f"activity: {p.activityScore}, "
            f"workspace: {p.workspaceCount}"
        )

    return "\n".join(lines)
