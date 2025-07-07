from fastapi import FastAPI, HTTPException, Depends
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
from pydantic import BaseModel
from typing import List, Dict
import uvicorn
import logging
from models import UserData, RecommendationRequest, RecommendationResponse, UserPurchase, UserPurchase
from recommendation import generate_recommendations, get_model_info, recommendation_engine
from data_loader import DataLoader

# Configuration du logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialisation du chargeur de données
data_loader = DataLoader()

@asynccontextmanager
async def lifespan(app: FastAPI):
    """Gestionnaire d'événements de cycle de vie de l'application"""
    # Démarrage
    try:
        # Charger les données et entraîner le modèle
        data_loader.load_games_data()
        data_loader.create_sample_users_data()
        recommendation_engine.train_model()
        logger.info("Application démarrée avec succès - Modèle KNN entraîné")
    except Exception as e:
        logger.error(f"Erreur lors du démarrage: {e}")
    
    yield
    
    # Arrêt
    logger.info("Application arrêtée")

# Création de l'application FastAPI
app = FastAPI(
    title="GamesUP Recommendation API",
    description="API de recommandation de jeux de société basée sur l'algorithme KNN",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc",
    lifespan=lifespan
)

# Configuration CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # En production, spécifier les domaines autorisés
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/", tags=["Statut"])
async def root():
    """Endpoint de base pour vérifier que l'API est en ligne"""
    return {
        "message": "API de recommandation GamesUP en ligne",
        "status": "active",
        "version": "1.0.0"
    }

@app.get("/health", tags=["Statut"])
async def health_check():
    """Vérification de l'état de santé de l'API"""
    try:
        model_info = get_model_info()
        return {
            "status": "healthy",
            "model_info": model_info,
            "timestamp": "2025-07-07T20:00:00Z"
        }
    except Exception as e:
        logger.error(f"Erreur lors du health check: {e}")
        raise HTTPException(status_code=500, detail="Service indisponible")

@app.get("/model/info", tags=["Modèle"])
async def get_model_information():
    """Récupère les informations sur le modèle de recommandation"""
    try:
        return get_model_info()
    except Exception as e:
        logger.error(f"Erreur lors de la récupération des infos du modèle: {e}")
        raise HTTPException(status_code=500, detail="Erreur lors de la récupération des informations du modèle")

@app.post("/recommendations/", response_model=RecommendationResponse, tags=["Recommandations"])
async def get_recommendations(request: RecommendationRequest):
    """
    Génère des recommandations de jeux pour un utilisateur
    
    - **user_data**: Données de l'utilisateur (achats, préférences)
    - **num_recommendations**: Nombre de recommandations souhaitées (défaut: 5)
    """
    try:
        logger.info(f"Demande de recommandations pour l'utilisateur {request.user_data.user_id}")
        
        # Validation des données
        if not request.user_data.purchases:
            raise HTTPException(status_code=400, detail="L'utilisateur doit avoir au moins un achat")
        
        if request.num_recommendations <= 0 or request.num_recommendations > 20:
            raise HTTPException(status_code=400, detail="Le nombre de recommandations doit être entre 1 et 20")
        
        # Générer les recommandations
        recommendations = generate_recommendations(request.user_data, request.num_recommendations)
        
        # Calculer les scores de confiance
        confidence_scores = [rec.get("confidence", 0.8) for rec in recommendations]
        
        # Construire la réponse
        response = RecommendationResponse(
            user_id=request.user_data.user_id,
            recommendations=recommendations,
            confidence_scores=confidence_scores,
            algorithm_used="KNN"
        )
        
        logger.info(f"Généré {len(recommendations)} recommandations pour l'utilisateur {request.user_data.user_id}")
        return response
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Erreur lors de la génération des recommandations: {e}")
        raise HTTPException(status_code=500, detail="Erreur interne du serveur")

@app.post("/recommendations/simple", tags=["Recommandations"])
async def get_simple_recommendations(user_data: UserData):
    """
    Endpoint simplifié pour les recommandations (compatibilité)
    """
    try:
        recommendations = generate_recommendations(user_data, 5)
        return {"recommendations": recommendations}
    except Exception as e:
        logger.error(f"Erreur lors de la génération des recommandations simples: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/recommendations/user/{user_id}", tags=["Recommandations"])
async def get_recommendations_for_user(user_id: int):
    """
    Génère des recommandations pour un utilisateur spécifique (compatibilité Spring Boot)
    """
    try:
        logger.info(f"Demande de recommandations pour l'utilisateur {user_id}")
        
        # Créer des données utilisateur simulées basées sur l'ID
        # En production, ces données viendraient de la base de données
        user_data = UserData(
            user_id=user_id,
            purchases=[
                UserPurchase(game_id=1, rating=4.5),
                UserPurchase(game_id=3, rating=4.0),
                UserPurchase(game_id=5, rating=3.5)
            ],
            preferences=None
        )
        
        # Générer les recommandations
        recommendations = generate_recommendations(user_data, 5)
        
        # Extraire les IDs des jeux recommandés
        game_ids = [rec.get("game_id", 1) for rec in recommendations]
        
        logger.info(f"Généré {len(game_ids)} recommandations pour l'utilisateur {user_id}")
        return game_ids
        
    except Exception as e:
        logger.error(f"Erreur lors de la génération des recommandations pour l'utilisateur {user_id}: {e}")
        # Retourner des recommandations par défaut en cas d'erreur
        return [1, 2, 3, 4, 5]

@app.get("/recommendations/game/{game_id}", tags=["Recommandations"])
async def get_recommendations_for_game(game_id: int):
    """
    Génère des recommandations basées sur un jeu spécifique (compatibilité Spring Boot)
    """
    try:
        logger.info(f"Demande de recommandations basées sur le jeu {game_id}")
        
        # Créer des données utilisateur simulées basées sur le jeu
        # En production, on analyserait les utilisateurs qui ont acheté ce jeu
        user_data = UserData(
            user_id=999,  # ID temporaire
            purchases=[
                UserPurchase(game_id=game_id, rating=4.0)
            ],
            preferences=None
        )
        
        # Générer les recommandations
        recommendations = generate_recommendations(user_data, 5)
        
        # Extraire les IDs des jeux recommandés
        game_ids = [rec.get("game_id", 1) for rec in recommendations]
        
        logger.info(f"Généré {len(game_ids)} recommandations basées sur le jeu {game_id}")
        return game_ids
        
    except Exception as e:
        logger.error(f"Erreur lors de la génération des recommandations pour le jeu {game_id}: {e}")
        # Retourner des recommandations par défaut en cas d'erreur
        return [1, 2, 3, 4, 5]

@app.post("/update-model", tags=["Modèle"])
async def update_model():
    """
    Met à jour le modèle de recommandation
    """
    try:
        logger.info("Mise à jour du modèle de recommandation")
        
        # Recharger les données et réentraîner le modèle
        data_loader.load_games_data()
        data_loader.create_sample_users_data()
        recommendation_engine.train_model()
        
        return {"message": "Modèle mis à jour avec succès", "status": "success"}
        
    except Exception as e:
        logger.error(f"Erreur lors de la mise à jour du modèle: {e}")
        raise HTTPException(status_code=500, detail="Erreur lors de la mise à jour du modèle")

@app.post("/train", tags=["Modèle"])
async def train_model():
    """
    Entraîne le modèle avec de nouvelles données
    """
    try:
        logger.info("Entraînement du modèle de recommandation")
        
        # Entraîner le modèle
        recommendation_engine.train_model()
        
        return {"message": "Modèle entraîné avec succès", "status": "success"}
        
    except Exception as e:
        logger.error(f"Erreur lors de l'entraînement du modèle: {e}")
        raise HTTPException(status_code=500, detail="Erreur lors de l'entraînement du modèle")

@app.get("/games/", tags=["Jeux"])
async def get_all_games():
    """Récupère la liste de tous les jeux disponibles"""
    try:
        games = []
        for game_id, game_data in data_loader.games_data.items():
            games.append({
                "game_id": game_id,
                "name": game_data.name,
                "category": game_data.category,
                "publisher": game_data.publisher,
                "price": game_data.price,
                "min_players": game_data.min_players,
                "max_players": game_data.max_players,
                "min_age": game_data.min_age,
                "duration": game_data.duration
            })
        return {"games": games, "total": len(games)}
    except Exception as e:
        logger.error(f"Erreur lors de la récupération des jeux: {e}")
        raise HTTPException(status_code=500, detail="Erreur lors de la récupération des jeux")

@app.get("/games/{game_id}", tags=["Jeux"])
async def get_game_by_id(game_id: int):
    """Récupère les détails d'un jeu spécifique"""
    try:
        if game_id not in data_loader.games_data:
            raise HTTPException(status_code=404, detail="Jeu non trouvé")
        
        game_data = data_loader.games_data[game_id]
        return {
            "game_id": game_id,
            "name": game_data.name,
            "category": game_data.category,
            "publisher": game_data.publisher,
            "price": game_data.price,
            "min_players": game_data.min_players,
            "max_players": game_data.max_players,
            "min_age": game_data.min_age,
            "duration": game_data.duration
        }
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Erreur lors de la récupération du jeu {game_id}: {e}")
        raise HTTPException(status_code=500, detail="Erreur lors de la récupération du jeu")

@app.get("/games/category/{category}", tags=["Jeux"])
async def get_games_by_category(category: str):
    """Récupère tous les jeux d'une catégorie spécifique"""
    try:
        games = []
        for game_id, game_data in data_loader.games_data.items():
            if game_data.category.lower() == category.lower():
                games.append({
                    "game_id": game_id,
                    "name": game_data.name,
                    "category": game_data.category,
                    "publisher": game_data.publisher,
                    "price": game_data.price
                })
        
        return {"games": games, "category": category, "total": len(games)}
    except Exception as e:
        logger.error(f"Erreur lors de la récupération des jeux par catégorie: {e}")
        raise HTTPException(status_code=500, detail="Erreur lors de la récupération des jeux")

@app.get("/stats", tags=["Statistiques"])
async def get_statistics():
    """Récupère des statistiques sur les données"""
    try:
        total_games = len(data_loader.games_data)
        total_users = len(data_loader.users_data)
        
        # Compter les jeux par catégorie
        categories = {}
        for game_data in data_loader.games_data.values():
            category = game_data.category
            categories[category] = categories.get(category, 0) + 1
        
        return {
            "total_games": total_games,
            "total_users": total_users,
            "games_by_category": categories,
            "model_info": get_model_info()
        }
    except Exception as e:
        logger.error(f"Erreur lors de la récupération des statistiques: {e}")
        raise HTTPException(status_code=500, detail="Erreur lors de la récupération des statistiques")

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8001)
