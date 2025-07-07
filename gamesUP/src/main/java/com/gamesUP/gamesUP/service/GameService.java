package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.dto.GameSearchDTO;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GameService {
    
    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final AvisRepository avisRepository;
    private final InventoryRepository inventoryRepository;
    
    public List<GameDTO> getAllGames() {
        log.info("Récupération de tous les jeux");
        return gameRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Page<GameDTO> getAllGamesPageable(int page, int size) {
        log.info("Récupération des jeux avec pagination - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("nom"));
        return gameRepository.findAll(pageable).map(this::convertToDTO);
    }
    
    public Optional<GameDTO> getGameById(Long id) {
        log.info("Récupération du jeu avec l'ID: {}", id);
        return gameRepository.findById(id).map(this::convertToDTO);
    }
    
    public List<GameDTO> getAvailableGames() {
        log.info("Récupération des jeux disponibles");
        return gameRepository.findByDisponible(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<GameDTO> searchGames(GameSearchDTO searchDTO) {
        log.info("Recherche de jeux avec les critères: {}", searchDTO);
        
        // Construction de la requête basée sur les critères
        List<Game> games = gameRepository.findByKeyword(searchDTO.getKeyword());
        
        // Filtrage par catégorie
        if (searchDTO.getCategoryId() != null) {
            games = games.stream()
                    .filter(game -> game.getCategory().getId().equals(searchDTO.getCategoryId()))
                    .collect(Collectors.toList());
        }
        
        // Filtrage par éditeur
        if (searchDTO.getPublisherId() != null) {
            games = games.stream()
                    .filter(game -> game.getPublisher().getId().equals(searchDTO.getPublisherId()))
                    .collect(Collectors.toList());
        }
        
        // Filtrage par prix
        if (searchDTO.getMinPrice() != null) {
            games = games.stream()
                    .filter(game -> game.getPrix().compareTo(searchDTO.getMinPrice()) >= 0)
                    .collect(Collectors.toList());
        }
        
        if (searchDTO.getMaxPrice() != null) {
            games = games.stream()
                    .filter(game -> game.getPrix().compareTo(searchDTO.getMaxPrice()) <= 0)
                    .collect(Collectors.toList());
        }
        
        // Filtrage par âge
        if (searchDTO.getMinAge() != null) {
            games = games.stream()
                    .filter(game -> game.getAgeMinimum() >= searchDTO.getMinAge())
                    .collect(Collectors.toList());
        }
        
        // Filtrage par nombre de joueurs
        if (searchDTO.getMinPlayers() != null && searchDTO.getMaxPlayers() != null) {
            games = games.stream()
                    .filter(game -> game.getNombreJoueursMin() <= searchDTO.getMinPlayers() &&
                            game.getNombreJoueursMax() >= searchDTO.getMaxPlayers())
                    .collect(Collectors.toList());
        }
        
        // Filtrage par durée
        if (searchDTO.getMaxDuration() != null) {
            games = games.stream()
                    .filter(game -> game.getDureePartieMinutes() <= searchDTO.getMaxDuration())
                    .collect(Collectors.toList());
        }
        
        // Filtrage par disponibilité
        if (searchDTO.getDisponible() != null) {
            games = games.stream()
                    .filter(game -> game.isDisponible() == searchDTO.getDisponible())
                    .collect(Collectors.toList());
        }
        
        return games.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    public GameDTO createGame(GameDTO gameDTO) {
        log.info("Création d'un nouveau jeu: {}", gameDTO.getNom());
        
        Game game = convertToEntity(gameDTO);
        Game savedGame = gameRepository.save(game);
        
        // Créer l'inventaire associé
        Inventory inventory = Inventory.builder()
                .game(savedGame)
                .stock(0)
                .stockMinimum(5)
                .disponible(true)
                .build();
        inventoryRepository.save(inventory);
        
        return convertToDTO(savedGame);
    }
    
    public Optional<GameDTO> updateGame(Long id, GameDTO gameDTO) {
        log.info("Mise à jour du jeu avec l'ID: {}", id);
        
        return gameRepository.findById(id).map(existingGame -> {
            updateGameFromDTO(existingGame, gameDTO);
            Game updatedGame = gameRepository.save(existingGame);
            return convertToDTO(updatedGame);
        });
    }
    
    public boolean deleteGame(Long id) {
        log.info("Suppression du jeu avec l'ID: {}", id);
        
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<GameDTO> getGamesByCategory(Long categoryId) {
        log.info("Récupération des jeux de la catégorie: {}", categoryId);
        return gameRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<GameDTO> getGamesByPublisher(Long publisherId) {
        log.info("Récupération des jeux de l'éditeur: {}", publisherId);
        return gameRepository.findByPublisherId(publisherId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<GameDTO> getGamesByAuthor(String authorName) {
        log.info("Récupération des jeux de l'auteur: {}", authorName);
        return gameRepository.findByAuthorName(authorName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<GameDTO> getGamesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Récupération des jeux dans la fourchette de prix: {} - {}", minPrice, maxPrice);
        return gameRepository.findByPrixBetween(minPrice, maxPrice).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public long getAvailableGamesCount() {
        return gameRepository.countAvailableGames();
    }
    
    // Méthodes de conversion
    private GameDTO convertToDTO(Game game) {
        GameDTO dto = GameDTO.builder()
                .id(game.getId())
                .nom(game.getNom())
                .description(game.getDescription())
                .prix(game.getPrix())
                .numEdition(game.getNumEdition())
                .ageMinimum(game.getAgeMinimum())
                .nombreJoueursMin(game.getNombreJoueursMin())
                .nombreJoueursMax(game.getNombreJoueursMax())
                .dureePartieMinutes(game.getDureePartieMinutes())
                .disponible(game.isDisponible())
                .build();
        
        // Ajout des informations de catégorie
        if (game.getCategory() != null) {
            dto.setCategoryId(game.getCategory().getId());
            dto.setCategoryType(game.getCategory().getType());
        }
        
        // Ajout des informations d'éditeur
        if (game.getPublisher() != null) {
            dto.setPublisherId(game.getPublisher().getId());
            dto.setPublisherName(game.getPublisher().getName());
        }
        
        // Ajout des informations d'auteurs
        if (game.getAuthors() != null && !game.getAuthors().isEmpty()) {
            Set<Long> authorIds = game.getAuthors().stream()
                    .map(Author::getId)
                    .collect(Collectors.toSet());
            Set<String> authorNames = game.getAuthors().stream()
                    .map(Author::getName)
                    .collect(Collectors.toSet());
            dto.setAuthorIds(authorIds);
            dto.setAuthorNames(authorNames);
        }
        
        // Ajout des statistiques d'avis
        Double avgRating = avisRepository.getAverageRatingByGameId(game.getId());
        Long numberOfReviews = avisRepository.countApprovedByGameId(game.getId());
        dto.setAverageRating(avgRating);
        dto.setNumberOfReviews(numberOfReviews);
        
        // Ajout des informations de stock
        Optional<Inventory> inventory = inventoryRepository.findByGameId(game.getId());
        inventory.ifPresent(inv -> dto.setStock(inv.getStock()));
        
        return dto;
    }
    
    private Game convertToEntity(GameDTO dto) {
        Game game = Game.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .prix(dto.getPrix())
                .numEdition(dto.getNumEdition())
                .ageMinimum(dto.getAgeMinimum())
                .nombreJoueursMin(dto.getNombreJoueursMin())
                .nombreJoueursMax(dto.getNombreJoueursMax())
                .dureePartieMinutes(dto.getDureePartieMinutes())
                .disponible(dto.isDisponible())
                .build();
        
        // Association de la catégorie
        if (dto.getCategoryId() != null) {
            categoryRepository.findById(dto.getCategoryId()).ifPresent(game::setCategory);
        }
        
        // Association de l'éditeur
        if (dto.getPublisherId() != null) {
            publisherRepository.findById(dto.getPublisherId()).ifPresent(game::setPublisher);
        }
        
        // Association des auteurs
        if (dto.getAuthorIds() != null && !dto.getAuthorIds().isEmpty()) {
            Set<Author> authors = dto.getAuthorIds().stream()
                    .map(authorId -> authorRepository.findById(authorId).orElse(null))
                    .filter(author -> author != null)
                    .collect(Collectors.toSet());
            game.setAuthors(authors);
        }
        
        return game;
    }
    
    private void updateGameFromDTO(Game game, GameDTO dto) {
        game.setNom(dto.getNom());
        game.setDescription(dto.getDescription());
        game.setPrix(dto.getPrix());
        game.setNumEdition(dto.getNumEdition());
        game.setAgeMinimum(dto.getAgeMinimum());
        game.setNombreJoueursMin(dto.getNombreJoueursMin());
        game.setNombreJoueursMax(dto.getNombreJoueursMax());
        game.setDureePartieMinutes(dto.getDureePartieMinutes());
        game.setDisponible(dto.isDisponible());
        
        // Mise à jour de la catégorie
        if (dto.getCategoryId() != null) {
            categoryRepository.findById(dto.getCategoryId()).ifPresent(game::setCategory);
        }
        
        // Mise à jour de l'éditeur
        if (dto.getPublisherId() != null) {
            publisherRepository.findById(dto.getPublisherId()).ifPresent(game::setPublisher);
        }
        
        // Mise à jour des auteurs
        if (dto.getAuthorIds() != null) {
            Set<Author> authors = dto.getAuthorIds().stream()
                    .map(authorId -> authorRepository.findById(authorId).orElse(null))
                    .filter(author -> author != null)
                    .collect(Collectors.toSet());
            game.setAuthors(authors);
        }
    }
} 