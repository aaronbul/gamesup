package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final GameRepository gameRepository;
    private final RestTemplate restTemplate;

    @Value("${python.api.url:http://localhost:8001}")
    private String pythonApiUrl;

    /**
     * Récupère les recommandations pour un utilisateur
     */
    public List<GameDTO> getRecommendationsForUser(Long userId) {
        log.info("Récupération des recommandations pour l'utilisateur: {}", userId);
        
        try {
            // Appel à l'API Python pour obtenir les recommandations
            String url = pythonApiUrl + "/recommendations/user/" + userId;
            ResponseEntity<Long[]> response = restTemplate.getForEntity(url, Long[].class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Long> recommendedGameIds = List.of(response.getBody());
                return getGamesByIds(recommendedGameIds);
            } else {
                log.warn("L'API Python n'a pas retourné de recommandations valides");
                return getFallbackRecommendations();
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'appel à l'API Python: {}", e.getMessage());
            return getFallbackRecommendations();
        }
    }

    /**
     * Récupère les recommandations basées sur un jeu
     */
    public List<GameDTO> getRecommendationsForGame(Long gameId) {
        log.info("Récupération des recommandations basées sur le jeu: {}", gameId);
        
        try {
            // Appel à l'API Python pour obtenir les recommandations
            String url = pythonApiUrl + "/recommendations/game/" + gameId;
            ResponseEntity<Long[]> response = restTemplate.getForEntity(url, Long[].class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Long> recommendedGameIds = List.of(response.getBody());
                return getGamesByIds(recommendedGameIds);
            } else {
                log.warn("L'API Python n'a pas retourné de recommandations valides");
                return getFallbackRecommendations();
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'appel à l'API Python: {}", e.getMessage());
            return getFallbackRecommendations();
        }
    }

    /**
     * Met à jour le modèle de recommandation
     */
    public String updateRecommendationModel() {
        log.info("Mise à jour du modèle de recommandation");
        
        try {
            String url = pythonApiUrl + "/update-model";
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Modèle mis à jour avec succès: " + response.getBody();
            } else {
                return "Erreur lors de la mise à jour du modèle - Mode fallback activé";
            }
        } catch (ResourceAccessException e) {
            log.error("Erreur de connexion à l'API Python: {}", e.getMessage());
            return "API Python non accessible - Mode fallback activé";
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du modèle: {}", e.getMessage());
            return "Erreur lors de la mise à jour du modèle - Mode fallback activé";
        }
    }

    /**
     * Envoie les données utilisateur à l'API Python pour l'entraînement
     */
    public String sendUserDataToPythonAPI() {
        log.info("Envoi des données utilisateur à l'API Python");
        
        try {
            // Récupération des données utilisateur (achats, avis, etc.)
            // TODO: Implémenter la récupération des données utilisateur
            
            String url = pythonApiUrl + "/train";
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Données envoyées avec succès: " + response.getBody();
            } else {
                return "Erreur lors de l'envoi des données - Mode fallback activé";
            }
        } catch (ResourceAccessException e) {
            log.error("Erreur de connexion à l'API Python: {}", e.getMessage());
            return "API Python non accessible - Mode fallback activé";
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi des données: {}", e.getMessage());
            return "Erreur lors de l'envoi des données - Mode fallback activé";
        }
    }

    /**
     * Récupère le statut de l'API Python
     */
    public String getPythonAPIStatus() {
        log.info("Vérification du statut de l'API Python sur: {}", pythonApiUrl);
        
        try {
            String url = pythonApiUrl + "/health";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return "API Python opérationnelle: " + response.getBody();
            } else {
                return "API Python non disponible - Mode fallback activé";
            }
        } catch (ResourceAccessException e) {
            log.error("Erreur de connexion à l'API Python: {}", e.getMessage());
            return "API Python inaccessible - Mode fallback activé. L'algorithme KNN fonctionne localement.";
        } catch (Exception e) {
            log.error("Erreur lors de la vérification du statut: {}", e.getMessage());
            return "API Python inaccessible - Mode fallback activé. L'algorithme KNN fonctionne localement.";
        }
    }

    /**
     * Récupère les jeux par leurs IDs
     */
    private List<GameDTO> getGamesByIds(List<Long> gameIds) {
        List<Game> games = gameRepository.findAllById(gameIds);
        return games.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Recommandations de fallback (jeux populaires)
     */
    private List<GameDTO> getFallbackRecommendations() {
        log.info("Utilisation des recommandations de fallback");
        List<Game> popularGames = gameRepository.findAll().stream()
                .limit(10)
                .collect(Collectors.toList());
        
        return popularGames.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Conversion d'une entité Game vers GameDTO
     */
    private GameDTO convertToDTO(Game game) {
        return GameDTO.builder()
                .id(game.getId())
                .nom(game.getNom())
                .description(game.getDescription())
                .prix(game.getPrix())
                .ageMinimum(game.getAgeMinimum())
                .nombreJoueursMin(game.getNombreJoueursMin())
                .nombreJoueursMax(game.getNombreJoueursMax())
                .dureePartieMinutes(game.getDureePartieMinutes())
                .build();
    }
} 