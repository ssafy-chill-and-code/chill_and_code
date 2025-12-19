# llm_client.py
import requests
import os
import json
from dotenv import load_dotenv
from typing import List

load_dotenv()

GMS_URL = "https://gms.ssafy.io/gmsapi/api.openai.com/v1/chat/completions"


def build_fallback(places: List) -> dict:
    """
    LLM 실패 시 rule 기반 추천 사유 생성
    (점수 재계산 ❌, 기존 feature만 사용)
    """
    recommendations = []

    for p in places:
        if p.natureScore >= p.activityScore and p.natureScore >= 0.5:
            reason = "자연 점수가 높은 장소로 힐링형 워케이션에 적합합니다."
            tags = ["자연", "힐링"]
        elif p.activityScore >= 0.5:
            reason = "활동 요소가 풍부해 다양한 경험을 할 수 있는 장소입니다."
            tags = ["활동", "체험"]
        elif p.workspaceCount >= 5:
            reason = "업무 환경이 잘 갖춰져 집중하기 좋은 장소입니다."
            tags = ["업무", "집중"]
        else:
            reason = "종합 점수가 우수한 장소로 추천됩니다."
            tags = ["추천"]

        recommendations.append({
            "placeId": p.placeId,
            "tags": tags,
            "reasonText": reason
        })

    return {"recommendations": recommendations}


def call_llm(prompt: str, places: List) -> dict:
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
        # 외부 LLM 호출 타임아웃: 기본 4초 (환경변수 GMS_TIMEOUT_SEC로 조정 가능)
        timeout_sec = int(os.getenv("GMS_TIMEOUT_SEC", "4"))
        res = requests.post(
            GMS_URL,
            headers=headers,
            json=body,
            timeout=timeout_sec
        )
        content = res.json()["choices"][0]["message"]["content"]
        return json.loads(content)   # ✅ LLM 정상 응답

    except Exception:
        # ✅ LLM 실패 → fallback
        return build_fallback(places)
