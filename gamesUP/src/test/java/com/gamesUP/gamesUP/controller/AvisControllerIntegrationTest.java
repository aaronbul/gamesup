package com.gamesUP.gamesUP.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AvisControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllAvisNonAccessibleSansAuth() throws Exception {
        mockMvc.perform(get("/api/avis"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client", roles = {"CLIENT"})
    void getAllAvisAccessibleAvecClient() throws Exception {
        mockMvc.perform(get("/api/avis"))
                .andExpect(status().isOk());
    }

    @Test
    void getAvisByIdNonAccessibleSansAuth() throws Exception {
        mockMvc.perform(get("/api/avis/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client", roles = {"CLIENT"})
    void getAvisByIdAccessibleAvecClient() throws Exception {
        mockMvc.perform(get("/api/avis/1"))
                .andExpect(status().isOk());
    }
} 