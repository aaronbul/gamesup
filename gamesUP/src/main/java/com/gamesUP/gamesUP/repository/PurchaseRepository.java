package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    
    List<Purchase> findByUserId(Long userId);
    
    List<Purchase> findByStatus(Purchase.PurchaseStatus status);
    
    List<Purchase> findByPaid(boolean paid);
    
    List<Purchase> findByDelivered(boolean delivered);
    
    List<Purchase> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT p FROM Purchase p WHERE p.user.id = :userId ORDER BY p.date DESC")
    List<Purchase> findByUserIdOrderByDateDesc(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Purchase p WHERE p.user.id = :userId AND p.status = :status")
    List<Purchase> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Purchase.PurchaseStatus status);
    
    @Query("SELECT p FROM Purchase p WHERE p.paid = false AND p.status != 'CANCELLED'")
    List<Purchase> findUnpaidPurchases();
    
    @Query("SELECT p FROM Purchase p WHERE p.delivered = false AND p.status = 'SHIPPED'")
    List<Purchase> findShippedButNotDelivered();
    
    @Query("SELECT p FROM Purchase p WHERE p.user.id = :userId")
    Page<Purchase> findByUserIdPageable(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Purchase p WHERE p.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(p.totalAmount) FROM Purchase p WHERE p.user.id = :userId AND p.paid = true")
    Double getTotalSpentByUser(@Param("userId") Long userId);
} 