package com.gamesUP.gamesUP.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.dto.GameSearchDTO;
import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repository.CategoryRepository;
import com.gamesUP.gamesUP.repository.PublisherRepository;
import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private PublisherRepository publisherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private GameDTO testGameDTO;
    private Category testCategory;
    private Publisher testPublisher;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .type("Stratégie")
                .description("Jeux de stratégie")
                .build();

        testPublisher = Publisher.builder()
                .id(1L)
                .name("Asmodee")
                .description("Éditeur de jeux")
                .build();

        testGameDTO = GameDTO.builder()
                .id(1L)
                .nom("Catan")
                .description("Jeu de colonisation")
                .prix(new BigDecimal("45.00"))
                .numEdition(1)
                .ageMinimum(10)
                .nombreJoueursMin(3)
                .nombreJoueursMax(4)
                .dureePartieMinutes(90)
                .disponible(true)
                .categoryId(1L)
                .categoryType("Stratégie")
                .publisherId(1L)
                .publisherName("Asmodee")
                .build();
    }

    @Test
    void getAllGames_ShouldReturnGames() throws Exception {
        // Given
        List<GameDTO> games = Arrays.asList(testGameDTO);
        when(gameService.getAllGames()).thenReturn(games);

        // When & Then
        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nom").value("Catan"))
                .andExpect(jsonPath("$[0].prix").value(45.00));
    }

    @Test
    void getGameById_WhenGameExists_ShouldReturnGame() throws Exception {
        // Given
        when(gameService.getGameById(1L)).thenReturn(Optional.of(testGameDTO));

        // When & Then
        mockMvc.perform(get("/api/games/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Catan"));
    }

    @Test
    void getGameById_WhenGameDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        when(gameService.getGameById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/games/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchGames_WithKeyword_ShouldReturnFilteredGames() throws Exception {
        // Given
        GameSearchDTO searchDTO = GameSearchDTO.builder()
                .keyword("Catan")
                .build();
        List<GameDTO> games = Arrays.asList(testGameDTO);
        when(gameService.searchGames(any(GameSearchDTO.class))).thenReturn(games);

        // When & Then
        mockMvc.perform(post("/api/games/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nom").value("Catan"));
    }

    @Test
    void searchGamesByKeyword_ShouldReturnFilteredGames() throws Exception {
        // Given
        List<GameDTO> games = Arrays.asList(testGameDTO);
        when(gameService.searchGames(any(GameSearchDTO.class))).thenReturn(games);

        // When & Then
        mockMvc.perform(get("/api/games/search")
                        .param("keyword", "Catan"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nom").value("Catan"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGame_WithValidData_ShouldReturnCreatedGame() throws Exception {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(testPublisher));
        when(gameService.createGame(any(GameDTO.class))).thenReturn(testGameDTO);

        // When & Then
        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGameDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Catan"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createGame_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        GameDTO invalidGame = GameDTO.builder()
                .nom("") // Nom vide
                .prix(new BigDecimal("-10")) // Prix négatif
                .build();

        // When & Then
        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidGame)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGame_WhenGameExists_ShouldReturnUpdatedGame() throws Exception {
        // Given
        when(gameService.updateGame(eq(1L), any(GameDTO.class))).thenReturn(Optional.of(testGameDTO));

        // When & Then
        mockMvc.perform(put("/api/games/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGameDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nom").value("Catan"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGame_WhenGameDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        when(gameService.updateGame(eq(999L), any(GameDTO.class))).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/games/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGameDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGame_WhenGameExists_ShouldReturnNoContent() throws Exception {
        // Given
        when(gameService.deleteGame(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/games/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGame_WhenGameDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        when(gameService.deleteGame(999L)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/games/999"))
                .andExpect(status().isNotFound());
    }
} 