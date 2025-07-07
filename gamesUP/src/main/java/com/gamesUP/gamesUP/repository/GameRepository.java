package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    
    Optional<Game> findByNom(String nom);
    
    List<Game> findByDisponible(boolean disponible);
    
    List<Game> findByCategoryId(Long categoryId);
    
    List<Game> findByPublisherId(Long publisherId);
    
    List<Game> findByPrixBetween(BigDecimal minPrix, BigDecimal maxPrix);
    
    List<Game> findByAgeMinimumLessThanEqual(int age);
    
    List<Game> findByNombreJoueursMinLessThanEqualAndNombreJoueursMaxGreaterThanEqual(int min, int max);
    
    @Query("SELECT g FROM Game g WHERE g.nom LIKE %:keyword% OR g.description LIKE %:keyword%")
    List<Game> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT g FROM Game g WHERE g.nom LIKE %:keyword% OR g.description LIKE %:keyword%")
    Page<Game> findByKeywordPageable(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT g FROM Game g JOIN g.authors a WHERE a.name LIKE %:authorName%")
    List<Game> findByAuthorName(@Param("authorName") String authorName);
    
    @Query("SELECT g FROM Game g WHERE g.prix <= :maxPrice AND g.disponible = true")
    List<Game> findAvailableGamesByMaxPrice(@Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT g FROM Game g WHERE g.dureePartieMinutes <= :maxDuration")
    List<Game> findByMaxDuration(@Param("maxDuration") int maxDuration);
    
    @Query("SELECT g FROM Game g WHERE g.disponible = true ORDER BY g.prix ASC")
    List<Game> findAvailableGamesOrderByPrice();
    
    @Query("SELECT g FROM Game g WHERE g.disponible = true ORDER BY g.nom ASC")
    List<Game> findAvailableGamesOrderByName();
    
    @Query("SELECT COUNT(g) FROM Game g WHERE g.disponible = true")
    long countAvailableGames();
} 