package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.model.Avis;
import com.gamesUP.gamesUP.service.AvisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/avis")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AvisController {

    private final AvisService avisService;

    /**
     * Récupère tous les avis
     */
    @GetMapping
    public ResponseEntity<List<Avis>> getAllAvis() {
        log.info("GET /api/avis - Récupération de tous les avis");
        List<Avis> avis = avisService.getAllAvis();
        return ResponseEntity.ok(avis);
    }

    /**
     * Récupère un avis par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Avis> getAvisById(@PathVariable Long id) {
        log.info("GET /api/avis/{} - Récupération de l'avis", id);
        return avisService.getAvisById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les avis d'un jeu
     */
    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<Avis>> getAvisByGame(@PathVariable Long gameId) {
        log.info("GET /api/avis/game/{} - Récupération des avis du jeu", gameId);
        List<Avis> avis = avisService.getAvisByGame(gameId);
        return ResponseEntity.ok(avis);
    }

    /**
     * Récupère les avis d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Avis>> getAvisByUser(@PathVariable Long userId) {
        log.info("GET /api/avis/user/{} - Récupération des avis de l'utilisateur", userId);
        List<Avis> avis = avisService.getAvisByUser(userId);
        return ResponseEntity.ok(avis);
    }

    /**
     * Crée un nouvel avis
     */
    @PostMapping
    public ResponseEntity<Avis> createAvis(@Valid @RequestBody Avis avis) {
        log.info("POST /api/avis - Création d'un nouvel avis");
        try {
            Avis createdAvis = avisService.createAvis(avis);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAvis);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la création de l'avis: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour un avis
     */
    @PutMapping("/{id}")
    public ResponseEntity<Avis> updateAvis(@PathVariable Long id, @Valid @RequestBody Avis avis) {
        log.info("PUT /api/avis/{} - Mise à jour de l'avis", id);
        return avisService.updateAvis(id, avis)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un avis
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvis(@PathVariable Long id) {
        log.info("DELETE /api/avis/{} - Suppression de l'avis", id);
        if (avisService.deleteAvis(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 