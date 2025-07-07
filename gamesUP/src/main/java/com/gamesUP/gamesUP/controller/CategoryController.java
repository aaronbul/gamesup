package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.CategoryDTO;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Récupère toutes les catégories
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        log.info("GET /api/categories - Récupération de toutes les catégories");
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> CategoryDTO.builder()
                        .id(category.getId())
                        .type(category.getType())
                        .description(category.getDescription())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    /**
     * Récupère une catégorie par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        log.info("GET /api/categories/{} - Récupération de la catégorie", id);
        return categoryService.getCategoryById(id)
                .map(category -> CategoryDTO.builder()
                        .id(category.getId())
                        .type(category.getType())
                        .description(category.getDescription())
                        .build())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle catégorie
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        log.info("POST /api/categories - Création d'une nouvelle catégorie: {}", category.getType());
        try {
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la création de la catégorie: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour une catégorie
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category) {
        log.info("PUT /api/categories/{} - Mise à jour de la catégorie", id);
        return categoryService.updateCategory(id, category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une catégorie
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("DELETE /api/categories/{} - Suppression de la catégorie", id);
        if (categoryService.deleteCategory(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 