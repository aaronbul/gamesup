package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    /**
     * Récupère toutes les commandes
     */
    public List<Purchase> getAllPurchases() {
        log.info("Récupération de toutes les commandes");
        return purchaseRepository.findAll();
    }

    /**
     * Récupère une commande par son ID
     */
    public Optional<Purchase> getPurchaseById(Long id) {
        log.info("Récupération de la commande avec l'ID: {}", id);
        return purchaseRepository.findById(id);
    }

    /**
     * Récupère les commandes d'un utilisateur
     */
    public List<Purchase> getPurchasesByUser(Long userId) {
        log.info("Récupération des commandes de l'utilisateur: {}", userId);
        return purchaseRepository.findByUserId(userId);
    }

    /**
     * Crée une nouvelle commande
     */
    public Purchase createPurchase(Purchase purchase) {
        log.info("Création d'une nouvelle commande pour l'utilisateur: {}", purchase.getUser().getId());
        return purchaseRepository.save(purchase);
    }

    /**
     * Met à jour une commande
     */
    public Optional<Purchase> updatePurchase(Long id, Purchase purchase) {
        log.info("Mise à jour de la commande avec l'ID: {}", id);
        return purchaseRepository.findById(id)
                .map(existingPurchase -> {
                    existingPurchase.setDate(purchase.getDate());
                    existingPurchase.setTotalAmount(purchase.getTotalAmount());
                    existingPurchase.setStatus(purchase.getStatus());
                    return purchaseRepository.save(existingPurchase);
                });
    }

    /**
     * Supprime une commande
     */
    public boolean deletePurchase(Long id) {
        log.info("Suppression de la commande avec l'ID: {}", id);
        if (purchaseRepository.existsById(id)) {
            purchaseRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 