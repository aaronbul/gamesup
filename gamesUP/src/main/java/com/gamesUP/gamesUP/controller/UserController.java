package com.gamesUP.gamesUP.controller;

import com.gamesUP.gamesUP.dto.UserDTO;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    /**
     * Récupère tous les utilisateurs (ADMIN seulement)
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("GET /api/users - Récupération de tous les utilisateurs");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Récupère un utilisateur par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} - Récupération de l'utilisateur", id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouvel utilisateur (inscription)
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("POST /api/users/register - Inscription d'un nouvel utilisateur: {}", userDTO.getEmail());
        try {
            UserDTO createdUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            log.error("Erreur lors de l'inscription: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour un utilisateur
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        log.info("PUT /api/users/{} - Mise à jour de l'utilisateur", id);
        return userService.updateUser(id, userDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un utilisateur
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} - Suppression de l'utilisateur", id);
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Change le rôle d'un utilisateur
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<Void> changeUserRole(@PathVariable Long id, @RequestParam User.UserRole newRole) {
        log.info("PUT /api/users/{}/role - Changement du rôle vers: {}", id, newRole);
        if (userService.changeUserRole(id, newRole)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Récupère les utilisateurs par rôle
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable User.UserRole role) {
        log.info("GET /api/users/role/{} - Récupération des utilisateurs par rôle", role);
        List<UserDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * Récupère les clients actifs
     */
    @GetMapping("/clients")
    public ResponseEntity<List<UserDTO>> getActiveClients() {
        log.info("GET /api/users/clients - Récupération des clients actifs");
        List<UserDTO> clients = userService.getActiveClients();
        return ResponseEntity.ok(clients);
    }

    /**
     * Récupère les administrateurs actifs
     */
    @GetMapping("/admins")
    public ResponseEntity<List<UserDTO>> getActiveAdmins() {
        log.info("GET /api/users/admins - Récupération des administrateurs actifs");
        List<UserDTO> admins = userService.getActiveAdmins();
        return ResponseEntity.ok(admins);
    }
} 