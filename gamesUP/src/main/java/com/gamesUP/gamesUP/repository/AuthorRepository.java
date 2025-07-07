package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    Optional<Author> findByName(String name);
    
    boolean existsByName(String name);
    
    List<Author> findByCountry(String country);
    
    @Query("SELECT a FROM Author a WHERE a.name LIKE %:keyword%")
    List<Author> findByKeyword(@Param("keyword") String keyword);
} 