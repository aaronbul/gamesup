package com.gamesUP.gamesUP.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllPurchasesNonAccessibleSansAuth() throws Exception {
        mockMvc.perform(get("/api/purchases"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllPurchasesAccessibleAvecAdmin() throws Exception {
        mockMvc.perform(get("/api/purchases"))
                .andExpect(status().isBadRequest());
    }
} 