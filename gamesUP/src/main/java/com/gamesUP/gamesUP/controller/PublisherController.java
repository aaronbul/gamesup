package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.service.PublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PublisherController {

    private final PublisherService publisherService;

    /**
     * Récupère tous les éditeurs
     */
    @GetMapping
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        log.info("GET /api/publishers - Récupération de tous les éditeurs");
        List<Publisher> publishers = publisherService.getAllPublishers();
        return ResponseEntity.ok(publishers);
    }

    /**
     * Récupère un éditeur par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        log.info("GET /api/publishers/{} - Récupération de l'éditeur", id);
        return publisherService.getPublisherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouvel éditeur
     */
    @PostMapping
    public ResponseEntity<Publisher> createPublisher(@Valid @RequestBody Publisher publisher) {
        log.info("POST /api/publishers - Création d'un nouvel éditeur: {}", publisher.getName());
        try {
            Publisher createdPublisher = publisherService.createPublisher(publisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPublisher);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la création de l'éditeur: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour un éditeur
     */
    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id, @Valid @RequestBody Publisher publisher) {
        log.info("PUT /api/publishers/{} - Mise à jour de l'éditeur", id);
        return publisherService.updatePublisher(id, publisher)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un éditeur
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        log.info("DELETE /api/publishers/{} - Suppression de l'éditeur", id);
        if (publisherService.deletePublisher(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 