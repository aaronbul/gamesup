package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublisherService {

    private final PublisherRepository publisherRepository;

    /**
     * Récupère tous les éditeurs
     */
    public List<Publisher> getAllPublishers() {
        log.info("Récupération de tous les éditeurs");
        return publisherRepository.findAll();
    }

    /**
     * Récupère un éditeur par son ID
     */
    public Optional<Publisher> getPublisherById(Long id) {
        log.info("Récupération de l'éditeur avec l'ID: {}", id);
        return publisherRepository.findById(id);
    }

    /**
     * Crée un nouvel éditeur
     */
    public Publisher createPublisher(Publisher publisher) {
        log.info("Création d'un nouvel éditeur: {}", publisher.getName());
        return publisherRepository.save(publisher);
    }

    /**
     * Met à jour un éditeur
     */
    public Optional<Publisher> updatePublisher(Long id, Publisher publisher) {
        log.info("Mise à jour de l'éditeur avec l'ID: {}", id);
        return publisherRepository.findById(id)
                .map(existingPublisher -> {
                    existingPublisher.setName(publisher.getName());
                    existingPublisher.setDescription(publisher.getDescription());
                    return publisherRepository.save(existingPublisher);
                });
    }

    /**
     * Supprime un éditeur
     */
    public boolean deletePublisher(Long id) {
        log.info("Suppression de l'éditeur avec l'ID: {}", id);
        if (publisherRepository.existsById(id)) {
            publisherRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 