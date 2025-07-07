package com.gamesUP.gamesUP.validation;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.dto.UserDTO;
import com.gamesUP.gamesUP.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validGameDTO_ShouldPassValidation() {
        // Given
        GameDTO gameDTO = GameDTO.builder()
                .nom("Catan")
                .description("Jeu de colonisation")
                .prix(new BigDecimal("45.00"))
                .numEdition(1)
                .ageMinimum(10)
                .nombreJoueursMin(3)
                .nombreJoueursMax(4)
                .dureePartieMinutes(90)
                .disponible(true)
                .build();

        // When
        Set<ConstraintViolation<GameDTO>> violations = validator.validate(gameDTO);

        // Then
        assertTrue(violations.isEmpty(), "Le DTO valide ne devrait pas avoir de violations");
    }

    @Test
    void invalidGameDTO_WithEmptyName_ShouldFailValidation() {
        // Given
        GameDTO gameDTO = GameDTO.builder()
                .nom("") // Nom vide - invalide
                .prix(new BigDecimal("45.00"))
                .build();

        // When
        Set<ConstraintViolation<GameDTO>> violations = validator.validate(gameDTO);

        // Then
        assertFalse(violations.isEmpty(), "Le DTO avec nom vide devrait avoir des violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nom")));
    }

    @Test
    void invalidGameDTO_WithNegativePrice_ShouldFailValidation() {
        // Given
        GameDTO gameDTO = GameDTO.builder()
                .nom("Catan")
                .prix(new BigDecimal("-10.00")) // Prix négatif - invalide
                .build();

        // When
        Set<ConstraintViolation<GameDTO>> violations = validator.validate(gameDTO);

        // Then
        assertFalse(violations.isEmpty(), "Le DTO avec prix négatif devrait avoir des violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("prix")));
    }

    @Test
    void invalidGameDTO_WithNullPrice_ShouldFailValidation() {
        // Given
        GameDTO gameDTO = GameDTO.builder()
                .nom("Catan")
                .prix(null) // Prix null - invalide
                .build();

        // When
        Set<ConstraintViolation<GameDTO>> violations = validator.validate(gameDTO);

        // Then
        assertFalse(violations.isEmpty(), "Le DTO avec prix null devrait avoir des violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("prix")));
    }

    @Test
    void validUserDTO_ShouldPassValidation() {
        // Given
        UserDTO userDTO = UserDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@email.com")
                .password("password123")
                .role(User.UserRole.CLIENT)
                .active(true)
                .build();

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // Then
        assertTrue(violations.isEmpty(), "Le DTO utilisateur valide ne devrait pas avoir de violations");
    }

    @Test
    void invalidUserDTO_WithInvalidEmail_ShouldFailValidation() {
        // Given
        UserDTO userDTO = UserDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("invalid-email") // Email invalide
                .password("password123")
                .role(User.UserRole.CLIENT)
                .active(true)
                .build();

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // Then
        assertFalse(violations.isEmpty(), "Le DTO avec email invalide devrait avoir des violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void invalidUserDTO_WithShortPassword_ShouldFailValidation() {
        // Given
        UserDTO userDTO = UserDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@email.com")
                .password("123") // Mot de passe trop court
                .role(User.UserRole.CLIENT)
                .active(true)
                .build();

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // Then
        assertFalse(violations.isEmpty(), "Le DTO avec mot de passe court devrait avoir des violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void invalidUserDTO_WithShortName_ShouldFailValidation() {
        // Given
        UserDTO userDTO = UserDTO.builder()
                .nom("A") // Nom trop court
                .prenom("Jean")
                .email("jean.dupont@email.com")
                .password("password123")
                .role(User.UserRole.CLIENT)
                .active(true)
                .build();

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // Then
        assertFalse(violations.isEmpty(), "Le DTO avec nom court devrait avoir des violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nom")));
    }

    @Test
    void invalidUserDTO_WithLongName_ShouldFailValidation() {
        // Given
        String longName = "A".repeat(51); // Nom trop long
        UserDTO userDTO = UserDTO.builder()
                .nom(longName)
                .prenom("Jean")
                .email("jean.dupont@email.com")
                .password("password123")
                .role(User.UserRole.CLIENT)
                .active(true)
                .build();

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // Then
        assertFalse(violations.isEmpty(), "Le DTO avec nom long devrait avoir des violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nom")));
    }

    @Test
    void invalidUserDTO_WithEmptyEmail_ShouldFailValidation() {
        // Given
        UserDTO userDTO = UserDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("") // Email vide
                .password("password123")
                .role(User.UserRole.CLIENT)
                .active(true)
                .build();

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // Then
        assertFalse(violations.isEmpty(), "Le DTO avec email vide devrait avoir des violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void invalidUserDTO_WithEmptyPassword_ShouldFailValidation() {
        // Given
        UserDTO userDTO = UserDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@email.com")
                .password("") // Mot de passe vide
                .role(User.UserRole.CLIENT)
                .active(true)
                .build();

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // Then
        assertFalse(violations.isEmpty(), "Le DTO avec mot de passe vide devrait avoir des violations");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }
} 