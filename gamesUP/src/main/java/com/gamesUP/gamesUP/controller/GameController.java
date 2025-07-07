package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.dto.GameSearchDTO;
import com.gamesUP.gamesUP.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;

    /**
     * Récupère tous les jeux
     */
    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames() {
        log.info("GET /api/games - Récupération de tous les jeux");
        List<GameDTO> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    /**
     * Récupère un jeu par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Long id) {
        log.info("GET /api/games/{} - Récupération du jeu", id);
        return gameService.getGameById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Recherche de jeux avec critères
     */
    @PostMapping("/search")
    public ResponseEntity<List<GameDTO>> searchGames(@RequestBody GameSearchDTO searchDTO) {
        log.info("POST /api/games/search - Recherche de jeux avec critères: {}", searchDTO);
        List<GameDTO> games = gameService.searchGames(searchDTO);
        return ResponseEntity.ok(games);
    }

    /**
     * Recherche de jeux par mot-clé
     */
    @GetMapping("/search")
    public ResponseEntity<List<GameDTO>> searchGamesByKeyword(@RequestParam String keyword) {
        log.info("GET /api/games/search - Recherche de jeux par mot-clé: {}", keyword);
        GameSearchDTO searchDTO = new GameSearchDTO();
        searchDTO.setKeyword(keyword);
        List<GameDTO> games = gameService.searchGames(searchDTO);
        return ResponseEntity.ok(games);
    }

    /**
     * Crée un nouveau jeu
     */
    @PostMapping
    public ResponseEntity<GameDTO> createGame(@Valid @RequestBody GameDTO gameDTO) {
        log.info("POST /api/games - Création d'un nouveau jeu: {}", gameDTO.getNom());
        try {
            GameDTO createdGame = gameService.createGame(gameDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
        } catch (Exception e) {
            log.error("Erreur lors de la création du jeu: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour un jeu existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> updateGame(@PathVariable Long id, @Valid @RequestBody GameDTO gameDTO) {
        log.info("PUT /api/games/{} - Mise à jour du jeu", id);
        return gameService.updateGame(id, gameDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un jeu
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        log.info("DELETE /api/games/{} - Suppression du jeu", id);
        if (gameService.deleteGame(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}