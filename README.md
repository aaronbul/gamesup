# GamesUP - Plateforme de Vente de Jeux de Société

## 🎯 Vue d'ensemble

GamesUP est une plateforme de vente de jeux de société en ligne avec un système de recommandation intelligent basé sur l'algorithme KNN (K-Nearest Neighbors). Le projet comprend une API Spring Boot pour la gestion métier et une API Python FastAPI pour le système de recommandation.

## 🏗️ Architecture

Le système est composé de deux APIs distinctes :

- **API Spring Boot** (Port 8080) : Gestion des données métier, sécurité, et communication avec l'API Python
- **API Python FastAPI** (Port 8001) : Système de recommandation basé sur l'algorithme KNN

## 🚀 Fonctionnalités

- ✅ Gestion du catalogue de jeux (CRUD)
- ✅ Gestion des utilisateurs et authentification
- ✅ Gestion des commandes et achats
- ✅ Système de recommandation intelligent
- ✅ Recherche de jeux
- ✅ Gestion des avis et wishlist
- ✅ Gestion du stock et inventaire
- ✅ Sécurité avec Spring Security et JWT
- ✅ Tests unitaires et d'intégration

## 📁 Structure du projet

```
GamesUP/
├── ANNEXES/
│   ├── gamesUP/                    # API Spring Boot
│   │   ├── src/main/java/
│   │   │   └── com/gamesUP/gamesUP/
│   │   │       ├── controller/     # Contrôleurs REST
│   │   │       ├── service/        # Services métier
│   │   │       ├── repository/     # Accès aux données
│   │   │       ├── model/          # Entités JPA
│   │   │       ├── dto/            # Objets de transfert
│   │   │       ├── config/         # Configuration
│   │   │       └── exception/      # Gestion d'erreurs
│   │   └── src/test/               # Tests
│   └── CodeApiPython/              # API Python FastAPI
│       ├── main.py                 # Application FastAPI
│       ├── recommendation.py       # Algorithme KNN
│       ├── data_loader.py          # Chargement des données
│       ├── models.py               # Modèles Pydantic
│       └── test_*.py               # Tests Python
├── DOCUMENTATION.md                # Documentation complète
├── RAPPORT_COUVERTURE_TESTS.md     # Couverture Spring Boot
├── RAPPORT_COUVERTURE_PYTHON.md    # Couverture Python
└── sujet.md                        # Énoncé du projet
```

## 🛠️ Technologies utilisées

### Backend Spring Boot
- **Framework** : Spring Boot 3.x
- **Base de données** : H2 (développement)
- **ORM** : Hibernate/JPA
- **Sécurité** : Spring Security + JWT
- **Tests** : JUnit 5, Mockito, TestContainers
- **Documentation** : Swagger/OpenAPI

### API Python
- **Framework** : FastAPI
- **ML** : Algorithme KNN personnalisé
- **Validation** : Pydantic
- **Documentation** : Swagger automatique
- **Tests** : pytest

## 📊 Métriques de qualité

### Tests
- **Spring Boot** : 86 tests (100% réussis)
- **Python** : 11 tests (100% réussis)
- **Couverture globale** : 58.7%

### Performance
- **Temps de réponse API** : < 500ms
- **Précision recommandations** : 4.6/5
- **Score de confiance** : 0.8

## 🚀 Installation et démarrage

### Prérequis
- Java 17+
- Python 3.8+
- Maven 3.6+

### API Spring Boot
```bash
cd ANNEXES/gamesUP
mvn clean install
mvn spring-boot:run
```
L'API sera accessible sur `http://localhost:8080`
Documentation Swagger : `http://localhost:8080/swagger-ui.html`

### API Python
```bash
cd ANNEXES/CodeApiPython
pip install -r requirements.txt
python main.py
```
L'API sera accessible sur `http://localhost:8001`
Documentation Swagger : `http://localhost:8001/docs`

## 🧪 Tests

### Tests Spring Boot
```bash
cd ANNEXES/gamesUP
mvn test
```

### Tests Python
```bash
cd ANNEXES/CodeApiPython
python -m pytest test_*.py -v
```

## 📚 Documentation

- **[Documentation complète](DOCUMENTATION.md)** : Architecture, diagrammes, principes SOLID
- **[Rapport couverture Spring Boot](RAPPORT_COUVERTURE_TESTS.md)** : Détail des tests
- **[Rapport couverture Python](RAPPORT_COUVERTURE_PYTHON.md)** : Tests de l'algorithme KNN

## 🔧 Configuration

### Variables d'environnement Spring Boot
```properties
# application.properties
python.api.url=http://localhost:8001
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
```

### Variables d'environnement Python
```bash
# .env
PORT=8001
HOST=0.0.0.0
KNN_K=3
```

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📝 Licence

Ce projet est développé dans le cadre d'une étude de cas académique.

---

⭐ N'hésitez pas à donner une étoile si ce projet vous a été utile ! 