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
class WishlistControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllWishlistNonAccessibleSansAuth() throws Exception {
        mockMvc.perform(get("/api/wishlist"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client", roles = {"CLIENT"})
    void getAllWishlistAccessibleAvecClient() throws Exception {
        mockMvc.perform(get("/api/wishlist"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWishlistByIdNonAccessibleSansAuth() throws Exception {
        mockMvc.perform(get("/api/wishlist/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client", roles = {"CLIENT"})
    void getWishlistByIdAccessibleAvecClient() throws Exception {
        mockMvc.perform(get("/api/wishlist/1"))
                .andExpect(status().isBadRequest());
    }
} 