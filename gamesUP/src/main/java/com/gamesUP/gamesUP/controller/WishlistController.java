package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * Récupère tous les éléments de wishlist
     */
    @GetMapping
    public ResponseEntity<List<Wishlist>> getAllWishlist() {
        log.info("GET /api/wishlist - Récupération de tous les éléments de wishlist");
        List<Wishlist> wishlist = wishlistService.getAllWishlist();
        return ResponseEntity.ok(wishlist);
    }

    /**
     * Récupère un élément de wishlist par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable Long id) {
        log.info("GET /api/wishlist/{} - Récupération de l'élément de wishlist", id);
        return wishlistService.getWishlistById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère la wishlist d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishlist>> getWishlistByUser(@PathVariable Long userId) {
        log.info("GET /api/wishlist/user/{} - Récupération de la wishlist de l'utilisateur", userId);
        List<Wishlist> wishlist = wishlistService.getWishlistByUser(userId);
        return ResponseEntity.ok(wishlist);
    }

    /**
     * Crée un nouvel élément de wishlist
     */
    @PostMapping
    public ResponseEntity<Wishlist> createWishlist(@Valid @RequestBody Wishlist wishlist) {
        log.info("POST /api/wishlist - Création d'un nouvel élément de wishlist");
        try {
            Wishlist createdWishlist = wishlistService.createWishlist(wishlist);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdWishlist);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la création de l'élément de wishlist: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour un élément de wishlist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Wishlist> updateWishlist(@PathVariable Long id, @Valid @RequestBody Wishlist wishlist) {
        log.info("PUT /api/wishlist/{} - Mise à jour de l'élément de wishlist", id);
        return wishlistService.updateWishlist(id, wishlist)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un élément de wishlist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable Long id) {
        log.info("DELETE /api/wishlist/{} - Suppression de l'élément de wishlist", id);
        if (wishlistService.deleteWishlist(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 