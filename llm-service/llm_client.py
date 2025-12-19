"""LLM 클라이언트 및 Fallback 생성 유틸리티"""
import requests
import os
import json
import random
from dotenv import load_dotenv
from typing import List, Optional

load_dotenv()

GMS_URL = "https://gms.ssafy.io/gmsapi/api.openai.com/v1/chat/completions"


def build_fallback(
    places: List,
    style: Optional[str] = None,
    budget: Optional[str] = None,
    transport: Optional[str] = None,
) -> dict:
    """
    LLM 실패 시 rule 기반 추천 사유 생성 (표현 다양화)
    - 점수 재계산 없음, 전달된 feature에 기반
    - style/budget/transport를 자연스럽게 반영(무조건 포함하지는 않음)
    - 문장 패턴과 어조를 랜덤화하여 반복감 감소
    """

    def budget_text(b: Optional[str]) -> Optional[str]:
        if not b:
            return None
        u = b.upper()
        if u == "LOW":
            return "가성비"
        if u == "MID":
            return "적당한 가격대"
        if u == "HIGH":
            return "프리미엄"
        return None

    def maybe_clause(options: List[str], prob: float = 0.6) -> str:
        return random.choice(options) if options and random.random() < prob else ""

    style_hint = None
    if style:
        s = style.upper()
        if s == "NATURE":
            style_hint = random.choice(["자연을 즐기는 분께", "힐링을 찾는 분께", "풍경을 좋아하는 분께"])
        elif s == "CAFE":
            style_hint = random.choice(["카페 탐방을 좋아하는 분께", "작업 겸 여가를 즐기는 분께", "조용한 공간을 선호하는 분께"])
        else:
            style_hint = random.choice(["새로운 경험을 찾는 분께", "활동적인 일정을 원한다면", "다양한 취향을 존중한다면"])

    btxt = budget_text(budget)
    transport_opts = []
    if transport:
        transport_opts = [
            f"{transport}로 접근성이 좋아 이동 부담이 적어요.",
            f"{transport} 이동도 수월해 일정 짜기 편합니다.",
            f"{transport} 기준으로도 방문 동선이 깔끔해요.",
        ]

    recommendations = []

    for p in places:
        # 태그 후보 구성
        tags = []
        nature_tag = ["자연", "힐링", "풍경"]
        activity_tag = ["활동", "체험", "관광"]
        work_tag = ["업무", "집중", "카페"]

        # 지배적인 특성 판별
        dominant = max(p.natureScore, p.activityScore, (1.0 if p.workspaceCount >= 5 else 0.0))

        fragments = []

        # 스타일 힌트(문두에 선택적 사용)
        if style_hint and random.random() < 0.5:
            fragments.append(style_hint + " ")

        if p.natureScore == dominant and p.natureScore >= 0.45:
            reason_pool = [
                "고즈넉한 풍경과 쉬어가기 좋은 리듬이 매력입니다.",
                "자연스러운 분위기 속에서 호흡을 고르기 좋아요.",
                "초록 기운과 여유로운 무드가 일정을 편안하게 만듭니다.",
                "바람과 풍광이 좋아 머물수록 만족스러워요.",
            ]
            fragments.append(random.choice(reason_pool))
            tags.extend(random.sample(nature_tag, k=2))
        elif p.activityScore == dominant and p.activityScore >= 0.45:
            reason_pool = [
                "체험 요소가 많아 하루가 알차게 흘러갑니다.",
                "살아있는 동선과 볼거리로 에너지를 채울 수 있어요.",
                "다양한 액티비티가 있어 지루할 틈이 없습니다.",
                "생동감 있는 경험이 이어져 만족도가 높아요.",
            ]
            fragments.append(random.choice(reason_pool))
            tags.extend(random.sample(activity_tag, k=2))
        elif p.workspaceCount >= 5:
            reason_pool = [
                "작업 환경이 안정적이라 집중하기 좋습니다.",
                "쾌적한 좌석과 분위기로 생산성을 끌어올리기 쉬워요.",
                "조용한 동선과 편의로 워케이션에 적합합니다.",
                "네트워킹과 업무를 병행하기 좋은 세팅이에요.",
            ]
            fragments.append(random.choice(reason_pool))
            tags.extend(random.sample(work_tag, k=2))
        else:
            reason_pool = [
                "리듬이 안정적이고 기본기가 좋은 곳입니다.",
                "동선과 분위기의 밸런스가 좋아 누구에게나 무난합니다.",
                "특색과 편의가 균형을 이루어 만족도가 높아요.",
                "전체적으로 컨디션 관리에 유리한 구성입니다.",
            ]
            fragments.append(random.choice(reason_pool))
            tags.append("추천")

        # 예산 맥락(확률적으로 포함)
        if btxt:
            fragments.append(maybe_clause([
                f"예산 면에서도 {btxt}에 잘 맞습니다.",
                f"가격대가 {btxt} 범위라 선택이 수월해요.",
                f"{btxt} 기준으로도 만족스러운 편입니다.",
            ], prob=0.65))

        # 이동 맥락(확률적으로 포함)
        if transport_opts:
            fragments.append(maybe_clause(transport_opts, prob=0.6))

        # 문장 조립: 빈 조각 제거 후, 자연스러운 공백 처리
        reason = " ".join([frag for frag in fragments if frag]).strip()

        # 태그 정리: 중복 제거 및 최대 3개
        dedup_tags = []
        for t in tags:
            if t not in dedup_tags:
                dedup_tags.append(t)
        if style:
            # 스타일 기반 태그를 가끔 추가
            style_tag_map = {"NATURE": "자연", "CAFE": "카페", "ACTIVITY": "활동"}
            st = style_tag_map.get(style.upper(), None)
            if st and st not in dedup_tags and random.random() < 0.5:
                dedup_tags.insert(0, st)
        dedup_tags = dedup_tags[:3] if len(dedup_tags) > 3 else dedup_tags

        recommendations.append({
            "placeId": p.placeId,
            "tags": dedup_tags or ["추천"],
            "reasonText": reason or "분위기와 편의가 균형을 이뤄 추천합니다.",
        })

    return {"recommendations": recommendations}


def call_llm(
    prompt: str,
    places: List,
    style: Optional[str] = None,
    budget: Optional[str] = None,
    transport: Optional[str] = None,
) -> dict:
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {os.getenv('GMS_KEY')}"
    }

    body = {
        "model": "gpt-5-mini",
        "messages": [
            {"role": "developer", "content": "반드시 JSON 형식으로만 응답하라."},
            {"role": "user", "content": prompt}
        ]
    }

    try:
        # 외부 LLM 호출 타임아웃: 기본 10초 (환경변수 GMS_TIMEOUT_SEC로 조정 가능)
        timeout_sec = int(os.getenv("GMS_TIMEOUT_SEC", "10"))
        res = requests.post(
            GMS_URL,
            headers=headers,
            json=body,
            timeout=timeout_sec
        )
        content = res.json()["choices"][0]["message"]["content"]
        return json.loads(content)   # ✅ LLM 정상 응답

    except Exception:
        # ✅ LLM 실패 → 다양화된 fallback
        return build_fallback(places, style=style, budget=budget, transport=transport)
