#!/usr/bin/env python3
"""
Script de test pour l'API de recommandation GamesUP
"""

import requests
import json
import time
from typing import Dict, Any

# Configuration
BASE_URL = "http://localhost:8001"
TIMEOUT = 10

def test_health_check():
    """Test de l'endpoint de santé"""
    print("🔍 Test de l'endpoint de santé...")
    try:
        response = requests.get(f"{BASE_URL}/health", timeout=TIMEOUT)
        if response.status_code == 200:
            data = response.json()
            print(f"✅ API en ligne - Statut: {data.get('status')}")
            return True
        else:
            print(f"❌ Erreur: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Erreur de connexion: {e}")
        return False

def test_model_info():
    """Test de l'endpoint d'informations du modèle"""
    print("\n🔍 Test des informations du modèle...")
    try:
        response = requests.get(f"{BASE_URL}/model/info", timeout=TIMEOUT)
        if response.status_code == 200:
            data = response.json()
            print(f"✅ Modèle KNN - K={data.get('k_value')}, Entraîné: {data.get('is_trained')}")
            print(f"   Jeux: {data.get('total_games')}, Utilisateurs: {data.get('total_users')}")
            return True
        else:
            print(f"❌ Erreur: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Erreur de connexion: {e}")
        return False

def test_get_games():
    """Test de l'endpoint de récupération des jeux"""
    print("\n🔍 Test de récupération des jeux...")
    try:
        response = requests.get(f"{BASE_URL}/games/", timeout=TIMEOUT)
        if response.status_code == 200:
            data = response.json()
            games = data.get('games', [])
            print(f"✅ {len(games)} jeux récupérés")
            for game in games[:3]:  # Afficher les 3 premiers
                print(f"   - {game['name']} ({game['category']})")
            return True
        else:
            print(f"❌ Erreur: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Erreur de connexion: {e}")
        return False

def test_get_game_by_id():
    """Test de l'endpoint de récupération d'un jeu spécifique"""
    print("\n🔍 Test de récupération d'un jeu spécifique...")
    try:
        response = requests.get(f"{BASE_URL}/games/1", timeout=TIMEOUT)
        if response.status_code == 200:
            data = response.json()
            print(f"✅ Jeu récupéré: {data['name']} - {data['publisher']}")
            return True
        else:
            print(f"❌ Erreur: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Erreur de connexion: {e}")
        return False

def test_get_games_by_category():
    """Test de l'endpoint de récupération des jeux par catégorie"""
    print("\n🔍 Test de récupération des jeux par catégorie...")
    try:
        response = requests.get(f"{BASE_URL}/games/category/Stratégie", timeout=TIMEOUT)
        if response.status_code == 200:
            data = response.json()
            games = data.get('games', [])
            print(f"✅ {len(games)} jeux de stratégie récupérés")
            for game in games:
                print(f"   - {game['name']}")
            return True
        else:
            print(f"❌ Erreur: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Erreur de connexion: {e}")
        return False

def test_recommendations():
    """Test de l'endpoint de recommandations"""
    print("\n🔍 Test de génération de recommandations...")
    
    # Données de test pour un utilisateur qui aime les jeux de stratégie
    test_data = {
        "user_data": {
            "user_id": 999,
            "purchases": [
                {"game_id": 1, "rating": 4.5},  # Catan
                {"game_id": 4, "rating": 4.8},  # 7 Wonders
                {"game_id": 6, "rating": 4.2}   # Dominion
            ]
        },
        "num_recommendations": 3
    }
    
    try:
        response = requests.post(
            f"{BASE_URL}/recommendations/",
            json=test_data,
            headers={"Content-Type": "application/json"},
            timeout=TIMEOUT
        )
        
        if response.status_code == 200:
            data = response.json()
            recommendations = data.get('recommendations', [])
            print(f"✅ {len(recommendations)} recommandations générées")
            for rec in recommendations:
                print(f"   - {rec['game_name']} (Score: {rec['predicted_rating']}, Confiance: {rec['confidence']})")
            return True
        else:
            print(f"❌ Erreur: {response.status_code}")
            print(f"   Réponse: {response.text}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Erreur de connexion: {e}")
        return False

def test_simple_recommendations():
    """Test de l'endpoint de recommandations simplifié"""
    print("\n🔍 Test de recommandations simplifiées...")
    
    test_data = {
        "user_id": 999,
        "purchases": [
            {"game_id": 2, "rating": 4.7},  # Pandemic
            {"game_id": 10, "rating": 4.3}  # The Crew
        ]
    }
    
    try:
        response = requests.post(
            f"{BASE_URL}/recommendations/simple",
            json=test_data,
            headers={"Content-Type": "application/json"},
            timeout=TIMEOUT
        )
        
        if response.status_code == 200:
            data = response.json()
            recommendations = data.get('recommendations', [])
            print(f"✅ {len(recommendations)} recommandations simplifiées générées")
            return True
        else:
            print(f"❌ Erreur: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Erreur de connexion: {e}")
        return False

def test_statistics():
    """Test de l'endpoint de statistiques"""
    print("\n🔍 Test de récupération des statistiques...")
    try:
        response = requests.get(f"{BASE_URL}/stats", timeout=TIMEOUT)
        if response.status_code == 200:
            data = response.json()
            print(f"✅ Statistiques récupérées:")
            print(f"   - Total jeux: {data.get('total_games')}")
            print(f"   - Total utilisateurs: {data.get('total_users')}")
            print(f"   - Jeux par catégorie: {data.get('games_by_category')}")
            return True
        else:
            print(f"❌ Erreur: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"❌ Erreur de connexion: {e}")
        return False

def run_all_tests():
    """Exécute tous les tests"""
    print("🚀 Démarrage des tests de l'API de recommandation GamesUP")
    print("=" * 60)
    
    tests = [
        ("Health Check", test_health_check),
        ("Model Info", test_model_info),
        ("Get Games", test_get_games),
        ("Get Game by ID", test_get_game_by_id),
        ("Get Games by Category", test_get_games_by_category),
        ("Recommendations", test_recommendations),
        ("Simple Recommendations", test_simple_recommendations),
        ("Statistics", test_statistics)
    ]
    
    passed = 0
    total = len(tests)
    
    for test_name, test_func in tests:
        try:
            if test_func():
                passed += 1
            else:
                print(f"⚠️  Test '{test_name}' a échoué")
        except Exception as e:
            print(f"❌ Erreur lors du test '{test_name}': {e}")
    
    print("\n" + "=" * 60)
    print(f"📊 Résultats: {passed}/{total} tests réussis")
    
    if passed == total:
        print("🎉 Tous les tests sont passés avec succès!")
    else:
        print("⚠️  Certains tests ont échoué")
    
    return passed == total

if __name__ == "__main__":
    # Attendre un peu que l'API démarre
    print("⏳ Attente du démarrage de l'API...")
    time.sleep(2)
    
    success = run_all_tests()
    exit(0 if success else 1) 