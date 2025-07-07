package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    Optional<Inventory> findByGameId(Long gameId);
    
    List<Inventory> findByDisponible(boolean disponible);
    
    List<Inventory> findByStockLessThanEqual(int stock);
    
    @Query("SELECT i FROM Inventory i WHERE i.stock <= i.stockMinimum")
    List<Inventory> findLowStockItems();
    
    @Query("SELECT i FROM Inventory i WHERE i.stock = 0")
    List<Inventory> findOutOfStockItems();
    
    @Query("SELECT i FROM Inventory i WHERE i.stock > 0 AND i.disponible = true")
    List<Inventory> findAvailableItems();
    
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.stock <= i.stockMinimum")
    long countLowStockItems();
    
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.stock = 0")
    long countOutOfStockItems();
} 