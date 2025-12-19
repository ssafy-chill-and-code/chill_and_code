from fastapi import FastAPI
from pydantic import BaseModel
from typing import Annotated, List
from pydantic import Field
from prompt import build_prompt
from llm_client import call_llm

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


@app.post("/llm/recommend")
def recommend(req: RecommendRequest):
    prompt = build_prompt(
        style=req.userContext.style,
        budget=req.userContext.budget,
        places=req.places  
    )
    llm_result = call_llm(prompt)
    return {"status": "ok", "llmResult": llm_result}