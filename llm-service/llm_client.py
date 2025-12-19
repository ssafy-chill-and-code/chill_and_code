import requests
import os
from dotenv import load_dotenv

load_dotenv()

GMS_URL = "https://gms.ssafy.io/gmsapi/api.openai.com/v1/chat/completions"

def call_llm(prompt: str) -> str:
    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {os.getenv('GMS_KEY')}"
    }

    body = {
        "model": "gpt-5-mini",
        "messages": [
            {"role": "developer", "content": "너는 추천 이유를 생성하는 AI이다. 한국어로 답해라."},
            {"role": "user", "content": prompt}
        ]
    }

    res = requests.post(GMS_URL, headers=headers, json=body, timeout=10)
    return res.json()["choices"][0]["message"]["content"]
