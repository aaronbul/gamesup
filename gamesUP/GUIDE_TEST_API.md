# Guide de Test de l'API GamesUP

## Comment savoir si l'API fonctionne bien ?

### 1. Tests Automatisés

#### Exécution des tests unitaires et d'intégration
```bash
# Dans le répertoire du projet
mvn test
```

**Résultats attendus :**
- ✅ Tests unitaires : 100% de succès
- ✅ Tests d'intégration : 100% de succès  
- ✅ Tests de sécurité : 100% de succès
- ✅ Tests de validation : 100% de succès

#### Couverture de code
```bash
mvn jacoco:report
```
Le rapport sera généré dans `target/site/jacoco/index.html`

### 2. Test Manuel de l'API

#### Prérequis
1. **Démarrer l'application :**
   ```bash
   mvn spring-boot:run
   ```
   
2. **Vérifier que l'application démarre :**
   - L'application doit démarrer sur `http://localhost:8080`
   - Aucune erreur dans les logs
   - Base de données connectée

#### Scripts de test automatiques

**Sur Windows (PowerShell) :**
```powershell
.\test-api.ps1
```

**Sur Linux/Mac (Bash) :**
```bash
chmod +x test-api.sh
./test-api.sh
```

#### Tests manuels avec cURL

##### Endpoints Publics (sans authentification)

**1. Récupération de tous les jeux**
```bash
curl -X GET http://localhost:8080/api/games
```
**Attendu :** Status 200, liste des jeux (peut être vide)

**2. Récupération de toutes les catégories**
```bash
curl -X GET http://localhost:8080/api/categories
```
**Attendu :** Status 200, liste des catégories

**3. Recherche de jeux par mot-clé**
```bash
curl -X GET "http://localhost:8080/api/games/search?keyword=catan"
```
**Attendu :** Status 200, résultats de recherche

**4. Récupération d'un jeu par ID**
```bash
curl -X GET http://localhost:8080/api/games/1
```
**Attendu :** Status 404 (jeu inexistant) ou 200 (jeu trouvé)

**5. Inscription d'un utilisateur**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test",
    "prenom": "User", 
    "email": "test@test.com",
    "password": "password123"
  }'
```
**Attendu :** Status 201, utilisateur créé

**6. Vérification de l'existence d'un email**
```bash
curl -X GET http://localhost:8080/api/users/exists/test@test.com
```
**Attendu :** Status 200, `{"exists": true}`

##### Endpoints Protégés (nécessitent authentification)

**1. Création d'un jeu (ADMIN requis)**
```bash
curl -X POST http://localhost:8080/api/games \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test Game",
    "prix": 29.99,
    "description": "Jeu de test",
    "ageMinimum": 8,
    "nombreJoueursMin": 2,
    "nombreJoueursMax": 4,
    "dureePartieMinutes": 60
  }'
```
**Attendu :** Status 403 (accès refusé sans authentification)

**2. Récupération de tous les utilisateurs (ADMIN requis)**
```bash
curl -X GET http://localhost:8080/api/users
```
**Attendu :** Status 403 (accès refusé sans authentification)

### 3. Tests avec Postman

#### Collection Postman
Importez cette collection dans Postman :

```json
{
  "info": {
    "name": "GamesUP API Tests",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Public Endpoints",
      "item": [
        {
          "name": "Get All Games",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/games"
          }
        },
        {
          "name": "Get Categories",
          "request": {
            "method": "GET", 
            "url": "http://localhost:8080/api/categories"
          }
        },
        {
          "name": "Search Games",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/games/search?keyword=catan"
          }
        },
        {
          "name": "Register User",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/users/register",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\"nom\":\"Test\",\"prenom\":\"User\",\"email\":\"test@test.com\",\"password\":\"password123\"}"
            }
          }
        }
      ]
    },
    {
      "name": "Protected Endpoints",
      "item": [
        {
          "name": "Create Game (Should Fail)",
          "request": {
            "method": "POST",
            "url": "http://localhost:8080/api/games",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\"nom\":\"Test Game\",\"prix\":29.99,\"description\":\"Test\",\"ageMinimum\":8,\"nombreJoueursMin\":2,\"nombreJoueursMax\":4,\"dureePartieMinutes\":60}"
            }
          }
        },
        {
          "name": "Get Users (Should Fail)",
          "request": {
            "method": "GET",
            "url": "http://localhost:8080/api/users"
          }
        }
      ]
    }
  ]
}
```

### 4. Vérifications de Sécurité

#### Test des rôles et permissions

**1. Endpoints publics accessibles sans authentification :**
- ✅ `/api/games` - GET
- ✅ `/api/categories` - GET  
- ✅ `/api/games/search` - GET
- ✅ `/api/users/register` - POST
- ✅ `/api/users/exists/{email}` - GET

**2. Endpoints protégés inaccessibles sans authentification :**
- ✅ `/api/games` - POST (création)
- ✅ `/api/games/{id}` - PUT (modification)
- ✅ `/api/games/{id}` - DELETE (suppression)
- ✅ `/api/users` - GET (liste des utilisateurs)

**3. Gestion des erreurs :**
- ✅ Erreur 400 pour données invalides
- ✅ Erreur 403 pour accès refusé
- ✅ Erreur 404 pour ressources inexistantes
- ✅ Erreur 500 pour erreurs serveur

### 5. Tests de Performance

#### Test de charge simple
```bash
# Installer Apache Bench (ab)
# Sur Ubuntu/Debian : sudo apt-get install apache2-utils
# Sur Windows : télécharger depuis Apache

# Test de charge sur l'endpoint public
ab -n 100 -c 10 http://localhost:8080/api/games
```

**Métriques à surveiller :**
- Temps de réponse moyen < 200ms
- Taux de succès > 95%
- Pas d'erreurs 500

### 6. Tests de Base de Données

#### Vérification de la connexion
```bash
# Vérifier les logs au démarrage
# Devrait voir : "HikariPool-1 - Start completed"
```

#### Test des migrations
```bash
# Vérifier que les tables sont créées
# Dans les logs : "HHH000204: Processing PersistenceUnitInfo"
```

### 7. Checklist de Validation

#### ✅ Architecture
- [ ] Architecture en couches respectée (Controller → Service → Repository)
- [ ] Séparation des responsabilités
- [ ] Injection de dépendances

#### ✅ Sécurité
- [ ] Spring Security configuré
- [ ] Rôles CLIENT et ADMIN définis
- [ ] Endpoints protégés correctement
- [ ] CORS configuré
- [ ] CSRF désactivé pour l'API

#### ✅ Validation
- [ ] Validation des DTOs avec Bean Validation
- [ ] Messages d'erreur personnalisés
- [ ] Gestion globale des exceptions

#### ✅ Tests
- [ ] Tests unitaires pour les services
- [ ] Tests d'intégration pour les contrôleurs
- [ ] Tests de sécurité
- [ ] Tests de validation
- [ ] Couverture de code > 80%

#### ✅ Documentation
- [ ] Code documenté
- [ ] API RESTful
- [ ] Codes de statut HTTP appropriés

### 8. Dépannage

#### Problèmes courants

**1. Application ne démarre pas**
- Vérifier la configuration de la base de données
- Vérifier les logs d'erreur
- Vérifier les dépendances Maven

**2. Tests qui échouent**
- Vérifier la configuration de test (H2 en mémoire)
- Vérifier les mocks et stubs
- Vérifier les annotations de test

**3. Erreurs 500**
- Vérifier les logs d'erreur
- Vérifier la configuration de la base de données
- Vérifier les validations

**4. Erreurs 403**
- Vérifier la configuration Spring Security
- Vérifier les rôles et permissions
- Vérifier l'authentification

### 9. Métriques de Qualité

#### Objectifs de qualité
- **Couverture de tests :** > 80%
- **Temps de réponse :** < 200ms
- **Disponibilité :** > 99%
- **Sécurité :** Aucune vulnérabilité critique
- **Maintenabilité :** Code propre et documenté

#### Outils de monitoring
- **Logs :** Spring Boot Actuator
- **Métriques :** Micrometer
- **Santé :** `/actuator/health`
- **Info :** `/actuator/info`

---

**Note :** Ce guide doit être utilisé en complément des tests automatisés. Les tests manuels permettent de valider le comportement de l'API en conditions réelles. 