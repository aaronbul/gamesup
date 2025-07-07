# recommendation.py
import numpy as np
from typing import List, Dict, Tuple
from sklearn.neighbors import NearestNeighbors
from sklearn.metrics.pairwise import cosine_similarity
from models import UserData, GameData
from data_loader import DataLoader
import logging

# Configuration du logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class KNNRecommendationEngine:
    def __init__(self, k: int = 3):
        self.k = k
        self.data_loader = DataLoader()
        self.model = None
        self.is_trained = False
        
    def train_model(self):
        """Entraîne le modèle KNN avec les données d'exemple"""
        try:
            # Charger les données
            self.data_loader.load_games_data()
            self.data_loader.create_sample_users_data()
            
            # Construire la matrice des notes
            ratings_matrix = self.data_loader.build_ratings_matrix()
            
            # Entraîner le modèle KNN
            self.model = NearestNeighbors(n_neighbors=self.k, metric='cosine', algorithm='brute')
            self.model.fit(ratings_matrix)
            
            self.is_trained = True
            logger.info(f"Modèle KNN entraîné avec {len(ratings_matrix)} utilisateurs et {len(ratings_matrix[0])} jeux")
            
        except Exception as e:
            logger.error(f"Erreur lors de l'entraînement du modèle: {e}")
            raise
    
    def get_user_recommendations(self, user_data: UserData, num_recommendations: int = 5) -> List[Dict]:
        """Génère des recommandations pour un utilisateur donné"""
        if not self.is_trained:
            self.train_model()
        
        try:
            # Convertir les achats de l'utilisateur en vecteur
            user_vector = self.data_loader.get_user_vector(user_data.purchases)
            
            # Trouver les utilisateurs les plus similaires
            user_vector_reshaped = user_vector.reshape(1, -1)
            distances, indices = self.model.kneighbors(user_vector_reshaped)
            
            # Récupérer les notes des utilisateurs similaires
            similar_users_ratings = self.data_loader.ratings_matrix[indices[0]]
            
            # Calculer les recommandations basées sur les utilisateurs similaires
            recommendations = self._calculate_recommendations(
                user_vector, similar_users_ratings, num_recommendations
            )
            
            return recommendations
            
        except Exception as e:
            logger.error(f"Erreur lors de la génération des recommandations: {e}")
            # Retourner des recommandations par défaut en cas d'erreur
            return self._get_default_recommendations(num_recommendations)
    
    def _calculate_recommendations(self, user_vector: np.ndarray, 
                                 similar_users_ratings: np.ndarray, 
                                 num_recommendations: int) -> List[Dict]:
        """Calcule les recommandations basées sur les utilisateurs similaires"""
        # Trouver les jeux que l'utilisateur n'a pas encore joués
        unrated_games = np.where(user_vector == 0)[0]
        
        if len(unrated_games) == 0:
            return self._get_default_recommendations(num_recommendations)
        
        # Calculer les scores prédits pour les jeux non joués
        predicted_scores = []
        game_ids = list(self.data_loader.games_data.keys())
        
        for game_idx in unrated_games:
            # Moyenne pondérée des notes des utilisateurs similaires
            game_ratings = similar_users_ratings[:, game_idx]
            # Filtrer les notes non nulles
            valid_ratings = game_ratings[game_ratings > 0]
            
            if len(valid_ratings) > 0:
                predicted_score = np.mean(valid_ratings)
                predicted_scores.append((game_idx, predicted_score))
        
        # Trier par score prédit décroissant
        predicted_scores.sort(key=lambda x: x[1], reverse=True)
        
        # Construire la liste des recommandations
        recommendations = []
        for i, (game_idx, score) in enumerate(predicted_scores[:num_recommendations]):
            game_id = game_ids[game_idx]
            game_data = self.data_loader.games_data[game_id]
            
            recommendations.append({
                "game_id": game_id,
                "game_name": game_data.name,
                "category": game_data.category,
                "publisher": game_data.publisher,
                "price": game_data.price,
                "predicted_rating": round(score, 2),
                "confidence": self._calculate_confidence(score, len(predicted_scores))
            })
        
        return recommendations
    
    def _calculate_confidence(self, score: float, total_recommendations: int) -> float:
        """Calcule un score de confiance basé sur le score prédit et le nombre de recommandations"""
        # Confiance basée sur le score (plus le score est élevé, plus la confiance est élevée)
        score_confidence = min(score / 5.0, 1.0)
        
        # Confiance basée sur le nombre de recommandations disponibles
        availability_confidence = min(total_recommendations / 10.0, 1.0)
        
        # Moyenne pondérée
        confidence = (score_confidence * 0.7) + (availability_confidence * 0.3)
        return round(confidence, 2)
    
    def _get_default_recommendations(self, num_recommendations: int) -> List[Dict]:
        """Retourne des recommandations par défaut en cas d'erreur"""
        default_games = [
            {"game_id": 1, "game_name": "Catan", "category": "Stratégie", "publisher": "Asmodee", 
             "price": 45.0, "predicted_rating": 4.0, "confidence": 0.8},
            {"game_id": 2, "game_name": "Pandemic", "category": "Coopératif", "publisher": "Z-Man Games", 
             "price": 35.0, "predicted_rating": 4.2, "confidence": 0.8},
            {"game_id": 3, "game_name": "Ticket to Ride", "category": "Familial", "publisher": "Days of Wonder", 
             "price": 50.0, "predicted_rating": 4.1, "confidence": 0.8},
            {"game_id": 4, "game_name": "7 Wonders", "category": "Stratégie", "publisher": "Repos Production", 
             "price": 40.0, "predicted_rating": 4.3, "confidence": 0.8},
            {"game_id": 5, "game_name": "Carcassonne", "category": "Familial", "publisher": "Hans im Glück", 
             "price": 30.0, "predicted_rating": 4.0, "confidence": 0.8}
        ]
        
        return default_games[:num_recommendations]

# Instance globale du moteur de recommandation
recommendation_engine = KNNRecommendationEngine(k=3)

def generate_recommendations(user_data: UserData, num_recommendations: int = 5) -> List[Dict]:
    """Fonction principale pour générer des recommandations"""
    try:
        recommendations = recommendation_engine.get_user_recommendations(user_data, num_recommendations)
        logger.info(f"Généré {len(recommendations)} recommandations pour l'utilisateur {user_data.user_id}")
        return recommendations
    except Exception as e:
        logger.error(f"Erreur lors de la génération des recommandations: {e}")
        return recommendation_engine._get_default_recommendations(num_recommendations)

def get_model_info() -> Dict:
    """Retourne des informations sur le modèle"""
    return {
        "algorithm": "KNN (K-Nearest Neighbors)",
        "k_value": recommendation_engine.k,
        "is_trained": recommendation_engine.is_trained,
        "total_games": len(recommendation_engine.data_loader.games_data) if recommendation_engine.data_loader.games_data else 0,
        "total_users": len(recommendation_engine.data_loader.users_data) if recommendation_engine.data_loader.users_data else 0
    }
