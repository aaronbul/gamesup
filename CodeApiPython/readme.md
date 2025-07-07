# API de Recommandation GamesUP

## Description

API Python FastAPI pour le système de recommandation de jeux de société GamesUP. Cette API utilise l'algorithme KNN (K-Nearest Neighbors) pour générer des recommandations personnalisées basées sur les préférences des utilisateurs.

## Fonctionnalités

- **Algorithme KNN** : Recommandations basées sur la similarité des utilisateurs
- **Données de test** : Jeux et utilisateurs d'exemple inclus
- **API REST complète** : Endpoints pour recommandations, jeux, statistiques
- **Documentation automatique** : Swagger UI et ReDoc
- **Validation des données** : Avec Pydantic
- **Gestion d'erreurs** : Logging et réponses d'erreur appropriées

## Installation

### Prérequis

- Python 3.8+
- pip

### Installation des dépendances

```bash
pip install -r requirements.txt
```

## Lancement de l'API

### Mode développement

```bash
python main.py
```

### Mode production

```bash
uvicorn main:app --host 0.0.0.0 --port 8000
```

L'API sera accessible sur `http://localhost:8000`

## Documentation

- **Swagger UI** : `http://localhost:8000/docs`
- **ReDoc** : `http://localhost:8000/redoc`

## Endpoints

### Statut
- `GET /` - Vérification que l'API est en ligne
- `GET /health` - État de santé de l'API

### Modèle
- `GET /model/info` - Informations sur le modèle KNN

### Recommandations
- `POST /recommendations/` - Génération de recommandations personnalisées
- `POST /recommendations/simple` - Endpoint simplifié (compatibilité)

### Jeux
- `GET /games/` - Liste de tous les jeux
- `GET /games/{game_id}` - Détails d'un jeu spécifique
- `GET /games/category/{category}` - Jeux par catégorie

### Statistiques
- `GET /stats` - Statistiques sur les données

## Exemple d'utilisation

### Demande de recommandations

```bash
curl -X POST "http://localhost:8000/recommendations/" \
     -H "Content-Type: application/json" \
     -d '{
       "user_data": {
         "user_id": 1,
         "purchases": [
           {"game_id": 1, "rating": 4.5},
           {"game_id": 4, "rating": 4.8}
         ]
       },
       "num_recommendations": 5
     }'
```

### Réponse

```json
{
  "user_id": 1,
  "recommendations": [
    {
      "game_id": 6,
      "game_name": "Dominion",
      "category": "Stratégie",
      "publisher": "Rio Grande Games",
      "price": 35.0,
      "predicted_rating": 4.2,
      "confidence": 0.84
    }
  ],
  "confidence_scores": [0.84],
  "algorithm_used": "KNN"
}
```

## Architecture

### Structure des fichiers

```
CodeApiPython/
├── main.py              # Application FastAPI principale
├── models.py            # Modèles Pydantic pour la validation
├── data_loader.py       # Chargeur de données et données de test
├── recommendation.py    # Moteur de recommandation KNN
├── requirements.txt     # Dépendances Python
└── readme.md           # Documentation
```

### Algorithme KNN

1. **Préparation des données** : Construction de la matrice utilisateur-jeu
2. **Calcul de similarité** : Utilisation de la similarité cosinus
3. **Sélection des voisins** : K utilisateurs les plus similaires
4. **Prédiction** : Moyenne pondérée des notes des voisins
5. **Recommandation** : Tri par score prédit décroissant

### Données de test

L'API inclut 10 jeux de société populaires et 5 utilisateurs avec des préférences variées :

- **Jeux** : Catan, Pandemic, Ticket to Ride, 7 Wonders, etc.
- **Catégories** : Stratégie, Coopératif, Familial, Abstrait
- **Utilisateurs** : Préférences variées (stratégie, coopératif, familial, etc.)

## Configuration

### Paramètres du modèle KNN

- **K** : Nombre de voisins (défaut: 3)
- **Métrique** : Similarité cosinus
- **Algorithme** : Brute force pour la précision

### Variables d'environnement

- `PORT` : Port de l'API (défaut: 8000)
- `HOST` : Host de l'API (défaut: 0.0.0.0)

## Tests

Pour tester l'API, vous pouvez utiliser les exemples dans la documentation Swagger ou les requêtes curl fournies.

## Intégration avec Spring Boot

Cette API est conçue pour être appelée par l'API Spring Boot GamesUP via le contrôleur `RecommendationController`.

## Développement

### Ajout de nouveaux jeux

Modifiez la liste `test_games` dans `data_loader.py`

### Modification de l'algorithme

Modifiez la classe `KNNRecommendationEngine` dans `recommendation.py`

### Ajout d'endpoints

Ajoutez de nouveaux endpoints dans `main.py`

## Support

Pour toute question ou problème, consultez la documentation de l'API ou les logs de l'application.
