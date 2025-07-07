package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Avis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {
    
    List<Avis> findByGameId(Long gameId);
    
    List<Avis> findByUserId(Long userId);
    
    List<Avis> findByNote(int note);
    
    List<Avis> findByApprouve(boolean approuve);
    
    @Query("SELECT a FROM Avis a WHERE a.game.id = :gameId AND a.approuve = true ORDER BY a.dateCreation DESC")
    List<Avis> findApprovedByGameId(@Param("gameId") Long gameId);
    
    @Query("SELECT a FROM Avis a WHERE a.game.id = :gameId AND a.approuve = true")
    Page<Avis> findApprovedByGameIdPageable(@Param("gameId") Long gameId, Pageable pageable);
    
    @Query("SELECT AVG(a.note) FROM Avis a WHERE a.game.id = :gameId AND a.approuve = true")
    Double getAverageRatingByGameId(@Param("gameId") Long gameId);
    
    @Query("SELECT COUNT(a) FROM Avis a WHERE a.game.id = :gameId AND a.approuve = true")
    long countApprovedByGameId(@Param("gameId") Long gameId);
    
    @Query("SELECT a FROM Avis a WHERE a.user.id = :userId ORDER BY a.dateCreation DESC")
    List<Avis> findByUserIdOrderByDateDesc(@Param("userId") Long userId);
    
    @Query("SELECT a FROM Avis a WHERE a.note >= :minRating AND a.approuve = true")
    List<Avis> findByMinRating(@Param("minRating") int minRating);
} 