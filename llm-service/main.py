from fastapi import FastAPI
from pydantic import BaseModel
from typing import Annotated, List
from pydantic import Field

app = FastAPI()

class UserContext(BaseModel):
    style: str
    budget: str

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

from prompt import build_prompt

@app.post("/llm/recommend")
def recommend(req: RecommendRequest):
    prompt = build_prompt(
        style=req.userContext.style,
        budget=req.userContext.budget,
        places=req.places
    )
    return {
        "status": "ok",
        "promptPreview": prompt  # 테스트용
    }
