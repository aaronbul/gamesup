from pydantic import BaseModel
from typing import List, Optional
from datetime import datetime

class UserPurchase(BaseModel):
    game_id: int
    rating: float
    purchase_date: Optional[datetime] = None
    play_time: Optional[int] = None  # en minutes

class GameData(BaseModel):
    game_id: int
    name: str
    category: str
    publisher: str
    price: float
    min_players: int
    max_players: int
    min_age: int
    duration: int  # en minutes

class UserData(BaseModel):
    user_id: int
    purchases: List[UserPurchase]
    preferences: Optional[dict] = None  # préférences utilisateur

class RecommendationRequest(BaseModel):
    user_data: UserData
    num_recommendations: int = 5

class RecommendationResponse(BaseModel):
    user_id: int
    recommendations: List[dict]
    confidence_scores: List[float]
    algorithm_used: str = "KNN"
