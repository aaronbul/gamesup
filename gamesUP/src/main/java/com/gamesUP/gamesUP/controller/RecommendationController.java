package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Récupère des recommandations générales
     */
    @GetMapping
    public ResponseEntity<List<GameDTO>> getGeneralRecommendations() {
        log.info("GET /api/recommendations - Récupération des recommandations générales");
        try {
            // Utiliser l'utilisateur 1 par défaut pour les recommandations générales
            List<GameDTO> recommendations = recommendationService.getRecommendationsForUser(1L);
            return ResponseEntity.ok(recommendations);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la récupération des recommandations générales: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère les recommandations pour un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GameDTO>> getRecommendationsForUser(@PathVariable Long userId) {
        log.info("GET /api/recommendations/user/{} - Récupération des recommandations pour l'utilisateur", userId);
        try {
            List<GameDTO> recommendations = recommendationService.getRecommendationsForUser(userId);
            return ResponseEntity.ok(recommendations);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la récupération des recommandations: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère les recommandations basées sur un jeu
     */
    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<GameDTO>> getRecommendationsForGame(@PathVariable Long gameId) {
        log.info("GET /api/recommendations/game/{} - Récupération des recommandations basées sur le jeu", gameId);
        try {
            List<GameDTO> recommendations = recommendationService.getRecommendationsForGame(gameId);
            return ResponseEntity.ok(recommendations);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la récupération des recommandations: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Met à jour le modèle de recommandation
     */
    @PostMapping("/update-model")
    public ResponseEntity<String> updateRecommendationModel() {
        log.info("POST /api/recommendations/update-model - Mise à jour du modèle de recommandation");
        try {
            String result = recommendationService.updateRecommendationModel();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la mise à jour du modèle: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Envoie les données utilisateur à l'API Python pour l'entraînement
     */
    @PostMapping("/send-data")
    public ResponseEntity<String> sendUserDataToPythonAPI() {
        log.info("POST /api/recommendations/send-data - Envoi des données utilisateur à l'API Python");
        try {
            String result = recommendationService.sendUserDataToPythonAPI();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            log.error("Erreur lors de l'envoi des données: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère le statut de l'API Python
     */
    @GetMapping("/status")
    public ResponseEntity<String> getPythonAPIStatus() {
        log.info("GET /api/recommendations/status - Vérification du statut de l'API Python");
        try {
            String status = recommendationService.getPythonAPIStatus();
            return ResponseEntity.ok(status);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la vérification du statut: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
} 