package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.LoginRequestDTO;
import com.gamesUP.gamesUP.dto.LoginResponseDTO;
import com.gamesUP.gamesUP.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    /**
     * Authentification d'un utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("POST /api/auth/login - Tentative de connexion pour l'utilisateur: {}", loginRequest.getEmail());
        try {
            LoginResponseDTO response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la connexion: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Déconnexion d'un utilisateur
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        log.info("POST /api/auth/logout - Déconnexion de l'utilisateur");
        try {
            authService.logout(token);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Erreur lors de la déconnexion: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Vérification de la validité d'un token
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        log.info("GET /api/auth/validate - Vérification de la validité du token");
        try {
            boolean isValid = authService.validateToken(token);
            return ResponseEntity.ok(isValid);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la validation du token: {}", e.getMessage());
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Récupération des informations de l'utilisateur connecté
     */
    @GetMapping("/me")
    public ResponseEntity<LoginResponseDTO> getCurrentUser(@RequestHeader("Authorization") String token) {
        log.info("GET /api/auth/me - Récupération des informations de l'utilisateur connecté");
        try {
            LoginResponseDTO userInfo = authService.getCurrentUser(token);
            return ResponseEntity.ok(userInfo);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la récupération des informations utilisateur: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
} 