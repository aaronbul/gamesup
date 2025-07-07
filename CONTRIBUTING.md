# Guide de Contribution - GamesUP

Merci de votre intérêt pour contribuer au projet GamesUP ! Ce document fournit les lignes directrices pour contribuer au projet.

## 🎯 Comment contribuer

### Signaler un bug
1. Vérifiez que le bug n'a pas déjà été signalé dans les issues
2. Créez une nouvelle issue avec le label "bug"
3. Décrivez le problème de manière claire et détaillée
4. Incluez les étapes pour reproduire le bug
5. Ajoutez des captures d'écran si nécessaire

### Proposer une amélioration
1. Créez une issue avec le label "enhancement"
2. Décrivez l'amélioration proposée
3. Expliquez pourquoi cette amélioration serait utile
4. Proposez une approche d'implémentation si possible

### Contribuer au code
1. Fork le projet
2. Créez une branche pour votre feature
3. Committez vos changements
4. Poussez vers votre fork
5. Créez une Pull Request

## 🛠️ Configuration de l'environnement

### Prérequis
- Java 17+
- Python 3.8+
- Maven 3.6+
- Git

### Installation
```bash
# Cloner le projet
git clone https://github.com/votre-username/GamesUP.git
cd GamesUP

# Configuration Spring Boot
cd ANNEXES/gamesUP
mvn clean install

# Configuration Python
cd ../CodeApiPython
pip install -r requirements.txt
```

## 📝 Standards de code

### Java/Spring Boot
- Suivre les conventions de nommage Java
- Utiliser des annotations Spring appropriées
- Ajouter des commentaires Javadoc pour les méthodes publiques
- Respecter les principes SOLID
- Écrire des tests unitaires pour les nouvelles fonctionnalités

### Python
- Suivre PEP 8 pour le style de code
- Utiliser des docstrings pour les fonctions et classes
- Implémenter des tests avec pytest
- Utiliser des types hints quand c'est possible

### Tests
- Maintenir une couverture de tests > 80%
- Écrire des tests unitaires et d'intégration
- Utiliser des noms de tests descriptifs
- Tester les cas d'erreur et edge cases

## 🔄 Workflow Git

### Branches
- `main` : Code stable et production-ready
- `develop` : Branche de développement
- `feature/*` : Nouvelles fonctionnalités
- `bugfix/*` : Corrections de bugs
- `hotfix/*` : Corrections urgentes

### Messages de commit
Utilisez le format conventionnel :
```
type(scope): description

[optional body]

[optional footer]
```

Types :
- `feat` : Nouvelle fonctionnalité
- `fix` : Correction de bug
- `docs` : Documentation
- `style` : Formatage
- `refactor` : Refactoring
- `test` : Tests
- `chore` : Maintenance

Exemples :
```
feat(recommendation): add new KNN algorithm parameter
fix(auth): resolve JWT token validation issue
docs(api): update endpoint documentation
```

## 📋 Checklist Pull Request

Avant de soumettre une PR, vérifiez :

### Code
- [ ] Le code suit les standards de style
- [ ] Les tests passent
- [ ] La couverture de tests est maintenue
- [ ] Pas de code mort ou commenté

### Documentation
- [ ] README.md mis à jour si nécessaire
- [ ] Documentation API mise à jour
- [ ] Commentaires ajoutés pour le code complexe

### Tests
- [ ] Tests unitaires ajoutés
- [ ] Tests d'intégration si nécessaire
- [ ] Tests manuels effectués

### Sécurité
- [ ] Pas de données sensibles exposées
- [ ] Validation des entrées utilisateur
- [ ] Gestion d'erreurs appropriée

## 🚀 Processus de review

1. **Automatique** : Les tests CI/CD s'exécutent
2. **Review** : Au moins un maintainer doit approuver
3. **Merge** : Une fois approuvé, la PR est mergée

### Critères d'approbation
- Code fonctionnel et testé
- Respect des standards de qualité
- Documentation mise à jour
- Pas de régression introduite

## 🐛 Reporting de bugs

Template pour signaler un bug :

```markdown
**Description du bug**
Description claire et concise du problème.

**Étapes pour reproduire**
1. Aller à '...'
2. Cliquer sur '...'
3. Scroller jusqu'à '...'
4. Voir l'erreur

**Comportement attendu**
Description de ce qui devrait se passer.

**Comportement actuel**
Description de ce qui se passe actuellement.

**Captures d'écran**
Si applicable, ajoutez des captures d'écran.

**Environnement**
- OS : [ex: Windows 10, macOS, Linux]
- Navigateur : [ex: Chrome, Firefox, Safari]
- Version : [ex: 22]

**Informations supplémentaires**
Toute autre information pertinente.
```

## 💡 Proposer des améliorations

Template pour proposer une amélioration :

```markdown
**Problème**
Description claire du problème que cette amélioration résout.

**Solution proposée**
Description de la solution proposée.

**Alternatives considérées**
Autres solutions qui ont été considérées.

**Impact**
Impact de cette amélioration sur le projet.

**Implémentation**
Approche suggérée pour l'implémentation.
```

## 📞 Contact

Pour toute question ou clarification :
- Créez une issue sur GitHub
- Contactez les maintainers du projet

Merci de contribuer à GamesUP ! 🎮 