from fastapi import FastAPI
from pydantic import BaseModel
from typing import Annotated, List, Optional
from pydantic import Field
from prompt import build_prompt
from llm_client import call_llm
import logging


app = FastAPI()

class UserContext(BaseModel):
    style: str
    budget: str
    transport: Optional[str] = None

class PlaceInput(BaseModel):
    placeId: int
    name: str
    score: float
    natureScore: float
    activityScore: float
    workspaceCount: int

Places = Annotated[List[PlaceInput], Field(max_length=20)]

class RecommendRequest(BaseModel):
    userContext: UserContext
    places: Places


@app.post("/llm/recommend")
def recommend(req: RecommendRequest):
    prompt = build_prompt(
        style=req.userContext.style,
        budget=req.userContext.budget,
        transport=req.userContext.transport,
        places=req.places  
    )
    llm_result = call_llm(prompt, req.places)

    # Enforce transport inclusion in reasonText if provided
    transport = req.userContext.transport
    if transport:
        try:
            recs = llm_result.get("recommendations", [])
            for rec in recs:
                if not isinstance(rec, dict):
                    continue
                rt = rec.get("reasonText")
                if not isinstance(rt, str) or transport not in rt:
                    # Append a concise phrase that includes the literal transport
                    prefix = rt.rstrip() + " " if isinstance(rt, str) else ""
                    rec["reasonText"] = f"{prefix}{transport} 이동을 고려해 방문하기 좋습니다."
                    logging.warning("reasonText lacked transport; appended transport context.")
        except Exception as e:
            logging.warning("Failed to enforce transport in reasonText: %s", e)

    return {"status": "ok", "llmResult": llm_result}
