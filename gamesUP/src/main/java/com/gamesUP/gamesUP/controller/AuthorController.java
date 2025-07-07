package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthorController {

    private final AuthorService authorService;

    /**
     * Récupère tous les auteurs
     */
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        log.info("GET /api/authors - Récupération de tous les auteurs");
        List<Author> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    /**
     * Récupère un auteur par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        log.info("GET /api/authors/{} - Récupération de l'auteur", id);
        return authorService.getAuthorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouvel auteur
     */
    @PostMapping
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) {
        log.info("POST /api/authors - Création d'un nouvel auteur: {}", author.getName());
        try {
            Author createdAuthor = authorService.createAuthor(author);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la création de l'auteur: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour un auteur
     */
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Valid @RequestBody Author author) {
        log.info("PUT /api/authors/{} - Mise à jour de l'auteur", id);
        return authorService.updateAuthor(id, author)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un auteur
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.info("DELETE /api/authors/{} - Suppression de l'auteur", id);
        if (authorService.deleteAuthor(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 