package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.LoginRequestDTO;
import com.gamesUP.gamesUP.dto.LoginResponseDTO;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // Stockage temporaire des tokens (en production, utiliser Redis ou JWT)
    private final Map<String, User> activeTokens = new HashMap<>();

    /**
     * Authentification d'un utilisateur
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        log.info("Tentative de connexion pour l'utilisateur: {}", loginRequest.getEmail());
        
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        
        User user = userOpt.get();
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        
        if (!user.isActive()) {
            throw new RuntimeException("Compte désactivé");
        }
        
        // Génération d'un token simple (en production, utiliser JWT)
        String token = UUID.randomUUID().toString();
        activeTokens.put(token, user);
        
        return LoginResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .role(user.getRole())
                .message("Connexion réussie")
                .build();
    }

    /**
     * Déconnexion d'un utilisateur
     */
    public void logout(String token) {
        log.info("Déconnexion de l'utilisateur");
        String cleanToken = extractToken(token);
        activeTokens.remove(cleanToken);
    }

    /**
     * Vérification de la validité d'un token
     */
    public boolean validateToken(String token) {
        log.info("Vérification de la validité du token");
        String cleanToken = extractToken(token);
        return activeTokens.containsKey(cleanToken);
    }

    /**
     * Récupération des informations de l'utilisateur connecté
     */
    public LoginResponseDTO getCurrentUser(String token) {
        log.info("Récupération des informations de l'utilisateur connecté");
        String cleanToken = extractToken(token);
        
        User user = activeTokens.get(cleanToken);
        if (user == null) {
            throw new RuntimeException("Token invalide");
        }
        
        return LoginResponseDTO.builder()
                .token(cleanToken)
                .userId(user.getId())
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .role(user.getRole())
                .message("Utilisateur connecté")
                .build();
    }

    /**
     * Extraction du token depuis l'en-tête Authorization
     */
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return authorizationHeader;
    }
} 