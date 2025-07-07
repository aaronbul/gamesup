#!/usr/bin/env python3
"""
Script de test simple pour l'algorithme KNN
"""

from data_loader import DataLoader
from recommendation import KNNRecommendationEngine
from models import UserData, UserPurchase

def test_algorithm():
    """Test simple de l'algorithme KNN"""
    print("🧪 Test de l'algorithme KNN")
    print("=" * 50)
    
    try:
        # Initialiser le chargeur de données
        print("1. Initialisation du chargeur de données...")
        data_loader = DataLoader()
        
        # Charger les données
        print("2. Chargement des données...")
        games = data_loader.load_games_data()
        users = data_loader.create_sample_users_data()
        
        print(f"   ✅ {len(games)} jeux chargés")
        print(f"   ✅ {len(users)} utilisateurs chargés")
        
        # Construire la matrice des notes
        print("3. Construction de la matrice des notes...")
        ratings_matrix = data_loader.build_ratings_matrix()
        print(f"   ✅ Matrice {ratings_matrix.shape[0]}x{ratings_matrix.shape[1]} construite")
        
        # Initialiser le moteur de recommandation
        print("4. Initialisation du moteur KNN...")
        engine = KNNRecommendationEngine(k=3)
        engine.train_model()
        print("   ✅ Modèle KNN entraîné")
        
        # Tester avec un utilisateur
        print("5. Test de recommandations...")
        test_user = UserData(
            user_id=999,
            purchases=[
                UserPurchase(game_id=1, rating=4.5),  # Catan
                UserPurchase(game_id=4, rating=4.8),  # 7 Wonders
                UserPurchase(game_id=6, rating=4.2)   # Dominion
            ]
        )
        
        recommendations = engine.get_user_recommendations(test_user, 3)
        
        print(f"   ✅ {len(recommendations)} recommandations générées:")
        for i, rec in enumerate(recommendations, 1):
            print(f"      {i}. {rec['game_name']} (Score: {rec['predicted_rating']}, Confiance: {rec['confidence']})")
        
        # Test avec un autre utilisateur
        print("6. Test avec un utilisateur coopératif...")
        test_user2 = UserData(
            user_id=1000,
            purchases=[
                UserPurchase(game_id=2, rating=4.7),  # Pandemic
                UserPurchase(game_id=10, rating=4.3)  # The Crew
            ]
        )
        
        recommendations2 = engine.get_user_recommendations(test_user2, 3)
        
        print(f"   ✅ {len(recommendations2)} recommandations générées:")
        for i, rec in enumerate(recommendations2, 1):
            print(f"      {i}. {rec['game_name']} (Score: {rec['predicted_rating']}, Confiance: {rec['confidence']})")
        
        print("\n🎉 Tous les tests sont passés avec succès!")
        return True
        
    except Exception as e:
        print(f"\n❌ Erreur lors du test: {e}")
        import traceback
        traceback.print_exc()
        return False

if __name__ == "__main__":
    success = test_algorithm()
    exit(0 if success else 1) 