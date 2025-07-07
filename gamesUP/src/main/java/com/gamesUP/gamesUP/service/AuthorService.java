package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;

    /**
     * Récupère tous les auteurs
     */
    public List<Author> getAllAuthors() {
        log.info("Récupération de tous les auteurs");
        return authorRepository.findAll();
    }

    /**
     * Récupère un auteur par son ID
     */
    public Optional<Author> getAuthorById(Long id) {
        log.info("Récupération de l'auteur avec l'ID: {}", id);
        return authorRepository.findById(id);
    }

    /**
     * Crée un nouvel auteur
     */
    public Author createAuthor(Author author) {
        log.info("Création d'un nouvel auteur: {}", author.getName());
        return authorRepository.save(author);
    }

    /**
     * Met à jour un auteur
     */
    public Optional<Author> updateAuthor(Long id, Author author) {
        log.info("Mise à jour de l'auteur avec l'ID: {}", id);
        return authorRepository.findById(id)
                .map(existingAuthor -> {
                    existingAuthor.setName(author.getName());
                    existingAuthor.setBiography(author.getBiography());
                    return authorRepository.save(existingAuthor);
                });
    }

    /**
     * Supprime un auteur
     */
    public boolean deleteAuthor(Long id) {
        log.info("Suppression de l'auteur avec l'ID: {}", id);
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 