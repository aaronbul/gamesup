package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByType(String type);
    
    boolean existsByType(String type);
} 