package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    
    List<Wishlist> findByUserId(Long userId);
    
    List<Wishlist> findByGameId(Long gameId);
    
    Optional<Wishlist> findByUserIdAndGameId(Long userId, Long gameId);
    
    boolean existsByUserIdAndGameId(Long userId, Long gameId);
    
    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId ORDER BY w.priorite DESC, w.dateAjout DESC")
    List<Wishlist> findByUserIdOrderByPriorityAndDate(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.game.id = :gameId")
    long countByGameId(@Param("gameId") Long gameId);
} 