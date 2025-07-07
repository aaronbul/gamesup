package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PurchaseController {

    private final PurchaseService purchaseService;

    /**
     * Récupère toutes les commandes
     */
    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        log.info("GET /api/purchases - Récupération de toutes les commandes");
        List<Purchase> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    /**
     * Récupère une commande par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Long id) {
        log.info("GET /api/purchases/{} - Récupération de la commande", id);
        return purchaseService.getPurchaseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les commandes d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Purchase>> getPurchasesByUser(@PathVariable Long userId) {
        log.info("GET /api/purchases/user/{} - Récupération des commandes de l'utilisateur", userId);
        List<Purchase> purchases = purchaseService.getPurchasesByUser(userId);
        return ResponseEntity.ok(purchases);
    }

    /**
     * Crée une nouvelle commande
     */
    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@Valid @RequestBody Purchase purchase) {
        log.info("POST /api/purchases - Création d'une nouvelle commande");
        try {
            Purchase createdPurchase = purchaseService.createPurchase(purchase);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPurchase);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la création de la commande: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour une commande
     */
    @PutMapping("/{id}")
    public ResponseEntity<Purchase> updatePurchase(@PathVariable Long id, @Valid @RequestBody Purchase purchase) {
        log.info("PUT /api/purchases/{} - Mise à jour de la commande", id);
        return purchaseService.updatePurchase(id, purchase)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime une commande
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        log.info("DELETE /api/purchases/{} - Suppression de la commande", id);
        if (purchaseService.deletePurchase(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 