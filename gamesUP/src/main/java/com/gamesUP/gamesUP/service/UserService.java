package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.UserDTO;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<UserDTO> getAllUsers() {
        log.info("Récupération de tous les utilisateurs");
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<UserDTO> getUserById(Long id) {
        log.info("Récupération de l'utilisateur avec l'ID: {}", id);
        return userRepository.findById(id).map(this::convertToDTO);
    }
    
    public Optional<UserDTO> getUserByEmail(String email) {
        log.info("Récupération de l'utilisateur avec l'email: {}", email);
        return userRepository.findByEmail(email).map(this::convertToDTO);
    }
    
    public List<UserDTO> getUsersByRole(User.UserRole role) {
        log.info("Récupération des utilisateurs avec le rôle: {}", role);
        return userRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserDTO> getActiveUsers() {
        log.info("Récupération des utilisateurs actifs");
        return userRepository.findByActive(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserDTO> searchUsers(String keyword) {
        log.info("Recherche d'utilisateurs avec le mot-clé: {}", keyword);
        return userRepository.findByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Création d'un nouvel utilisateur: {}", userDTO.getEmail());
        
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        
        User user = convertToEntity(userDTO);
        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        log.info("Mise à jour de l'utilisateur avec l'ID: {}", id);
        
        return userRepository.findById(id).map(existingUser -> {
            // Vérifier si l'email change et s'il existe déjà
            if (!existingUser.getEmail().equals(userDTO.getEmail()) && 
                userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("Un utilisateur avec cet email existe déjà");
            }
            
            updateUserFromDTO(existingUser, userDTO);
            
            // Encoder le mot de passe seulement s'il a changé
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            
            User updatedUser = userRepository.save(existingUser);
            return convertToDTO(updatedUser);
        });
    }
    
    public boolean deleteUser(Long id) {
        log.info("Suppression de l'utilisateur avec l'ID: {}", id);
        
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean deactivateUser(Long id) {
        log.info("Désactivation de l'utilisateur avec l'ID: {}", id);
        
        return userRepository.findById(id).map(user -> {
            user.setActive(false);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }
    
    public boolean activateUser(Long id) {
        log.info("Activation de l'utilisateur avec l'ID: {}", id);
        
        return userRepository.findById(id).map(user -> {
            user.setActive(true);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }
    
    public boolean changeUserRole(Long id, User.UserRole newRole) {
        log.info("Changement du rôle de l'utilisateur avec l'ID: {} vers: {}", id, newRole);
        
        return userRepository.findById(id).map(user -> {
            user.setRole(newRole);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public List<UserDTO> getActiveClients() {
        log.info("Récupération des clients actifs");
        return userRepository.findActiveClients().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserDTO> getActiveAdmins() {
        log.info("Récupération des administrateurs actifs");
        return userRepository.findActiveAdmins().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Méthodes de conversion
    private UserDTO convertToDTO(User user) {
        UserDTO dto = UserDTO.builder()
                .id(user.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
        
        // Ajout des statistiques
        Long numberOfPurchases = purchaseRepository.countByUserId(user.getId());
        Double totalSpent = purchaseRepository.getTotalSpentByUser(user.getId());
        
        dto.setNumberOfPurchases(numberOfPurchases);
        dto.setTotalSpent(totalSpent != null ? totalSpent : 0.0);
        
        return dto;
    }
    
    private User convertToEntity(UserDTO dto) {
        return User.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .password(dto.getPassword()) // Sera encodé dans le service
                .role(dto.getRole())
                .active(dto.isActive())
                .build();
    }
    
    private void updateUserFromDTO(User user, UserDTO dto) {
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setActive(dto.isActive());
    }
} 