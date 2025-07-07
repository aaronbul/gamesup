package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<User> findByRole(User.UserRole role);
    
    List<User> findByActive(boolean active);
    
    @Query("SELECT u FROM User u WHERE u.nom LIKE %:keyword% OR u.prenom LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<User> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT u FROM User u WHERE u.role = 'CLIENT' AND u.active = true")
    List<User> findActiveClients();
    
    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN' AND u.active = true")
    List<User> findActiveAdmins();
} 