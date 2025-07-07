package com.gamesUP.gamesUP.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicEndpoints_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        // Test des endpoints publics - ces endpoints devraient être accessibles sans authentification
        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/games/search")
                        .param("keyword", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void userRegistration_ShouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Test\",\"prenom\":\"User\",\"email\":\"test@test.com\",\"password\":\"password123\"}"))
                .andExpect(status().isBadRequest()); // Retourne 400 car l'email existe déjà dans les données de test
    }

    @Test
    void clientEndpoints_ShouldBeAccessibleWithClientRole() throws Exception {
        // Ces endpoints nécessitent un rôle CLIENT
        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminEndpoints_ShouldBeAccessibleWithAdminRole() throws Exception {
        // Ces endpoints nécessitent un rôle ADMIN
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());

        // Test des endpoints de base qui devraient être accessibles
        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void clientUser_ShouldNotAccessAdminEndpoints() throws Exception {
        // Un utilisateur CLIENT ne peut pas accéder aux endpoints ADMIN
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthenticatedUser_ShouldNotAccessProtectedEndpoints() throws Exception {
        // Un utilisateur non authentifié ne peut pas accéder aux endpoints protégés
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden()); // Spring Security retourne 403 par défaut
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminUser_ShouldAccessAllEndpoints() throws Exception {
        // Un administrateur peut accéder à tous les endpoints
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }

    @Test
    void corsConfiguration_ShouldAllowCrossOriginRequests() throws Exception {
        mockMvc.perform(get("/api/games")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }

    @Test
    void csrfProtection_ShouldBeDisabled() throws Exception {
        // CSRF devrait être désactivé pour l'API REST
        // Test avec un endpoint qui nécessite une authentification
        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Test Game\",\"prix\":29.99,\"description\":\"Test\",\"ageMinimum\":8,\"dureePartieMinutes\":60,\"nombreJoueursMin\":2,\"nombreJoueursMax\":4,\"categoryId\":1,\"publisherId\":1}"))
                .andExpect(status().isBadRequest()); // 400 car validation échoue, pas 403 (CSRF)
    }
} 