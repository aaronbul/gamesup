package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.model.Avis;
import com.gamesUP.gamesUP.repository.AvisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvisService {

    private final AvisRepository avisRepository;

    /**
     * Récupère tous les avis
     */
    public List<Avis> getAllAvis() {
        log.info("Récupération de tous les avis");
        return avisRepository.findAll();
    }

    /**
     * Récupère un avis par son ID
     */
    public Optional<Avis> getAvisById(Long id) {
        log.info("Récupération de l'avis avec l'ID: {}", id);
        return avisRepository.findById(id);
    }

    /**
     * Récupère les avis d'un jeu
     */
    public List<Avis> getAvisByGame(Long gameId) {
        log.info("Récupération des avis du jeu: {}", gameId);
        return avisRepository.findByGameId(gameId);
    }

    /**
     * Récupère les avis d'un utilisateur
     */
    public List<Avis> getAvisByUser(Long userId) {
        log.info("Récupération des avis de l'utilisateur: {}", userId);
        return avisRepository.findByUserId(userId);
    }

    /**
     * Crée un nouvel avis
     */
    public Avis createAvis(Avis avis) {
        log.info("Création d'un nouvel avis pour le jeu: {}", avis.getGame().getId());
        return avisRepository.save(avis);
    }

    /**
     * Met à jour un avis
     */
    public Optional<Avis> updateAvis(Long id, Avis avis) {
        log.info("Mise à jour de l'avis avec l'ID: {}", id);
        return avisRepository.findById(id)
                .map(existingAvis -> {
                    existingAvis.setNote(avis.getNote());
                    existingAvis.setCommentaire(avis.getCommentaire());
                    existingAvis.setDateCreation(avis.getDateCreation());
                    return avisRepository.save(existingAvis);
                });
    }

    /**
     * Supprime un avis
     */
    public boolean deleteAvis(Long id) {
        log.info("Suppression de l'avis avec l'ID: {}", id);
        if (avisRepository.existsById(id)) {
            avisRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 