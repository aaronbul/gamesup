package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    /**
     * Récupère tous les éléments de wishlist
     */
    public List<Wishlist> getAllWishlist() {
        log.info("Récupération de tous les éléments de wishlist");
        return wishlistRepository.findAll();
    }

    /**
     * Récupère un élément de wishlist par son ID
     */
    public Optional<Wishlist> getWishlistById(Long id) {
        log.info("Récupération de l'élément de wishlist avec l'ID: {}", id);
        return wishlistRepository.findById(id);
    }

    /**
     * Récupère la wishlist d'un utilisateur
     */
    public List<Wishlist> getWishlistByUser(Long userId) {
        log.info("Récupération de la wishlist de l'utilisateur: {}", userId);
        return wishlistRepository.findByUserId(userId);
    }

    /**
     * Crée un nouvel élément de wishlist
     */
    public Wishlist createWishlist(Wishlist wishlist) {
        log.info("Création d'un nouvel élément de wishlist pour l'utilisateur: {}", wishlist.getUser().getId());
        return wishlistRepository.save(wishlist);
    }

    /**
     * Met à jour un élément de wishlist
     */
    public Optional<Wishlist> updateWishlist(Long id, Wishlist wishlist) {
        log.info("Mise à jour de l'élément de wishlist avec l'ID: {}", id);
        return wishlistRepository.findById(id)
                .map(existingWishlist -> {
                    existingWishlist.setDateAjout(wishlist.getDateAjout());
                    existingWishlist.setPriorite(wishlist.getPriorite());
                    return wishlistRepository.save(existingWishlist);
                });
    }

    /**
     * Supprime un élément de wishlist
     */
    public boolean deleteWishlist(Long id) {
        log.info("Suppression de l'élément de wishlist avec l'ID: {}", id);
        if (wishlistRepository.existsById(id)) {
            wishlistRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 