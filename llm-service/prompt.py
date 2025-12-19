def build_prompt(style: str, budget: str, places: list) -> str:
    lines = []
    lines.append(f"사용자 스타일: {style}")
    lines.append(f"예산: {budget}")
    lines.append("")
    lines.append("아래 장소는 이미 점수 계산과 정렬이 완료된 추천 결과이다.")
    lines.append("순위를 변경하지 말고, 점수를 다시 계산하지 마라.")
    lines.append("")
    lines.append("장소 정보:")

    for idx, p in enumerate(places, start=1):
        lines.append(
            f"{idx}. 이름: {p.name}, "
            f"점수: {p.score}, "
            f"자연: {p.natureScore}, "
            f"활동: {p.activityScore}, "
            f"업무: {p.workspaceCount}"
        )

    lines.append("")
    lines.append("각 장소에 대해:")
    lines.append("1. 추천 이유 1~2문장 생성")
    lines.append("2. 태그 3~5개 생성")

    return "\n".join(lines)
