import pandas as pd
import numpy as np
from typing import Dict, List, Tuple
from models import GameData, UserPurchase
import json

class DataLoader:
    def __init__(self):
        self.games_data = {}
        self.users_data = {}
        self.ratings_matrix = None
        
    def load_games_data(self, file_path: str = None) -> Dict[int, GameData]:
        """Charge les données des jeux depuis un fichier ou crée des données de test"""
        if file_path:
            df = pd.read_csv(file_path)
            for _, row in df.iterrows():
                self.games_data[row['game_id']] = GameData(
                    game_id=row['game_id'],
                    name=row['name'],
                    category=row['category'],
                    publisher=row['publisher'],
                    price=row['price'],
                    min_players=row['min_players'],
                    max_players=row['max_players'],
                    min_age=row['min_age'],
                    duration=row['duration']
                )
        else:
            # Créer des données de test
            test_games = [
                {"game_id": 1, "name": "Catan", "category": "Stratégie", "publisher": "Asmodee", 
                 "price": 45.0, "min_players": 3, "max_players": 4, "min_age": 10, "duration": 90},
                {"game_id": 2, "name": "Pandemic", "category": "Coopératif", "publisher": "Z-Man Games", 
                 "price": 35.0, "min_players": 2, "max_players": 4, "min_age": 8, "duration": 45},
                {"game_id": 3, "name": "Ticket to Ride", "category": "Familial", "publisher": "Days of Wonder", 
                 "price": 50.0, "min_players": 2, "max_players": 5, "min_age": 8, "duration": 60},
                {"game_id": 4, "name": "7 Wonders", "category": "Stratégie", "publisher": "Repos Production", 
                 "price": 40.0, "min_players": 3, "max_players": 7, "min_age": 10, "duration": 30},
                {"game_id": 5, "name": "Carcassonne", "category": "Familial", "publisher": "Hans im Glück", 
                 "price": 30.0, "min_players": 2, "max_players": 5, "min_age": 7, "duration": 45},
                {"game_id": 6, "name": "Dominion", "category": "Stratégie", "publisher": "Rio Grande Games", 
                 "price": 35.0, "min_players": 2, "max_players": 4, "min_age": 13, "duration": 30},
                {"game_id": 7, "name": "Splendor", "category": "Stratégie", "publisher": "Space Cowboys", 
                 "price": 40.0, "min_players": 2, "max_players": 4, "min_age": 10, "duration": 30},
                {"game_id": 8, "name": "Azul", "category": "Abstrait", "publisher": "Plan B Games", 
                 "price": 45.0, "min_players": 2, "max_players": 4, "min_age": 8, "duration": 45},
                {"game_id": 9, "name": "Wingspan", "category": "Stratégie", "publisher": "Stonemaier Games", 
                 "price": 55.0, "min_players": 1, "max_players": 5, "min_age": 10, "duration": 70},
                {"game_id": 10, "name": "The Crew", "category": "Coopératif", "publisher": "Kosmos", 
                 "price": 15.0, "min_players": 2, "max_players": 5, "min_age": 10, "duration": 20}
            ]
            
            for game in test_games:
                self.games_data[game["game_id"]] = GameData(**game)
        
        return self.games_data
    
    def create_sample_users_data(self) -> Dict[int, List[UserPurchase]]:
        """Crée des données d'utilisateurs de test avec des préférences variées"""
        sample_users = {
            1: [  # Utilisateur qui aime les jeux de stratégie
                UserPurchase(game_id=1, rating=4.5),  # Catan
                UserPurchase(game_id=4, rating=4.8),  # 7 Wonders
                UserPurchase(game_id=6, rating=4.2),  # Dominion
                UserPurchase(game_id=7, rating=4.6),  # Splendor
            ],
            2: [  # Utilisateur qui préfère les jeux coopératifs
                UserPurchase(game_id=2, rating=4.7),  # Pandemic
                UserPurchase(game_id=10, rating=4.3), # The Crew
                UserPurchase(game_id=3, rating=3.8),  # Ticket to Ride
            ],
            3: [  # Utilisateur qui aime les jeux familiaux
                UserPurchase(game_id=3, rating=4.4),  # Ticket to Ride
                UserPurchase(game_id=5, rating=4.1),  # Carcassonne
                UserPurchase(game_id=8, rating=4.0),  # Azul
            ],
            4: [  # Utilisateur avec des goûts variés
                UserPurchase(game_id=1, rating=4.0),  # Catan
                UserPurchase(game_id=2, rating=4.5),  # Pandemic
                UserPurchase(game_id=9, rating=4.8),  # Wingspan
                UserPurchase(game_id=10, rating=4.2), # The Crew
            ],
            5: [  # Utilisateur qui aime les jeux complexes
                UserPurchase(game_id=9, rating=4.9),  # Wingspan
                UserPurchase(game_id=4, rating=4.6),  # 7 Wonders
                UserPurchase(game_id=6, rating=4.7),  # Dominion
            ]
        }
        
        self.users_data = sample_users
        return self.users_data
    
    def build_ratings_matrix(self) -> np.ndarray:
        """Construit la matrice des notes utilisateur-jeu"""
        if not self.users_data or not self.games_data:
            raise ValueError("Les données utilisateurs et jeux doivent être chargées d'abord")
        
        user_ids = list(self.users_data.keys())
        game_ids = list(self.games_data.keys())
        
        # Créer la matrice des notes
        self.ratings_matrix = np.zeros((len(user_ids), len(game_ids)))
        
        for i, user_id in enumerate(user_ids):
            for j, game_id in enumerate(game_ids):
                # Chercher la note de l'utilisateur pour ce jeu
                for purchase in self.users_data[user_id]:
                    if purchase.game_id == game_id:
                        self.ratings_matrix[i, j] = purchase.rating
                        break
        
        return self.ratings_matrix
    
    def get_user_vector(self, user_purchases: List[UserPurchase]) -> np.ndarray:
        """Convertit les achats d'un utilisateur en vecteur de notes"""
        if not self.games_data:
            raise ValueError("Les données des jeux doivent être chargées d'abord")
        
        game_ids = list(self.games_data.keys())
        user_vector = np.zeros(len(game_ids))
        
        for purchase in user_purchases:
            if purchase.game_id in self.games_data:
                game_index = game_ids.index(purchase.game_id)
                user_vector[game_index] = purchase.rating
        
        return user_vector
