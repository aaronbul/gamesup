package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.GameDTO;
import com.gamesUP.gamesUP.dto.GameSearchDTO;
import com.gamesUP.gamesUP.model.*;
import com.gamesUP.gamesUP.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private PublisherRepository publisherRepository;
    
    @Mock
    private AuthorRepository authorRepository;
    
    @Mock
    private AvisRepository avisRepository;
    
    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private GameService gameService;

    private Game testGame;
    private GameDTO testGameDTO;
    private Category testCategory;
    private Publisher testPublisher;
    private Author testAuthor;

    @BeforeEach
    void setUp() {
        // Création des données de test
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

        testAuthor = Author.builder()
                .id(1L)
                .name("Klaus Teuber")
                .biography("Créateur de Catan")
                .build();

        testGame = Game.builder()
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
                .category(testCategory)
                .publisher(testPublisher)
                .authors(new HashSet<>(Arrays.asList(testAuthor)))
                .build();

        testGameDTO = GameDTO.builder()
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
                .publisherId(1L)
                .authorIds(new HashSet<>(Arrays.asList(1L)))
                .build();
    }

    @Test
    void getAllGames_ShouldReturnAllGames() {
        // Given
        List<Game> games = Arrays.asList(testGame);
        when(gameRepository.findAll()).thenReturn(games);
        when(avisRepository.getAverageRatingByGameId(anyLong())).thenReturn(4.5);
        when(avisRepository.countApprovedByGameId(anyLong())).thenReturn(10L);
        when(inventoryRepository.findByGameId(anyLong())).thenReturn(Optional.empty());

        // When
        List<GameDTO> result = gameService.getAllGames();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Catan", result.get(0).getNom());
        verify(gameRepository).findAll();
    }

    @Test
    void getAllGamesPageable_ShouldReturnPageOfGames() {
        // Given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nom"));
        Page<Game> gamePage = new PageImpl<>(Arrays.asList(testGame));
        when(gameRepository.findAll(pageable)).thenReturn(gamePage);
        when(avisRepository.getAverageRatingByGameId(anyLong())).thenReturn(4.5);
        when(avisRepository.countApprovedByGameId(anyLong())).thenReturn(10L);
        when(inventoryRepository.findByGameId(anyLong())).thenReturn(Optional.empty());

        // When
        Page<GameDTO> result = gameService.getAllGamesPageable(0, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Catan", result.getContent().get(0).getNom());
        verify(gameRepository).findAll(pageable);
    }

    @Test
    void getGameById_WhenGameExists_ShouldReturnGame() {
        // Given
        when(gameRepository.findById(1L)).thenReturn(Optional.of(testGame));
        when(avisRepository.getAverageRatingByGameId(1L)).thenReturn(4.5);
        when(avisRepository.countApprovedByGameId(1L)).thenReturn(10L);
        when(inventoryRepository.findByGameId(1L)).thenReturn(Optional.empty());

        // When
        Optional<GameDTO> result = gameService.getGameById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Catan", result.get().getNom());
        verify(gameRepository).findById(1L);
    }

    @Test
    void getGameById_WhenGameDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<GameDTO> result = gameService.getGameById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(gameRepository).findById(999L);
    }

    @Test
    void getAvailableGames_ShouldReturnOnlyAvailableGames() {
        // Given
        List<Game> availableGames = Arrays.asList(testGame);
        when(gameRepository.findByDisponible(true)).thenReturn(availableGames);
        when(avisRepository.getAverageRatingByGameId(anyLong())).thenReturn(4.5);
        when(avisRepository.countApprovedByGameId(anyLong())).thenReturn(10L);
        when(inventoryRepository.findByGameId(anyLong())).thenReturn(Optional.empty());

        // When
        List<GameDTO> result = gameService.getAvailableGames();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isDisponible());
        verify(gameRepository).findByDisponible(true);
    }

    @Test
    void createGame_ShouldCreateAndReturnGame() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(testPublisher));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(new Inventory());

        // When
        GameDTO result = gameService.createGame(testGameDTO);

        // Then
        assertNotNull(result);
        assertEquals("Catan", result.getNom());
        verify(gameRepository).save(any(Game.class));
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void updateGame_WhenGameExists_ShouldUpdateAndReturnGame() {
        // Given
        when(gameRepository.findById(1L)).thenReturn(Optional.of(testGame));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(testPublisher));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(gameRepository.save(any(Game.class))).thenReturn(testGame);

        // When
        Optional<GameDTO> result = gameService.updateGame(1L, testGameDTO);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Catan", result.get().getNom());
        verify(gameRepository).findById(1L);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void updateGame_WhenGameDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<GameDTO> result = gameService.updateGame(999L, testGameDTO);

        // Then
        assertFalse(result.isPresent());
        verify(gameRepository).findById(999L);
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void deleteGame_WhenGameExists_ShouldReturnTrue() {
        // Given
        when(gameRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = gameService.deleteGame(1L);

        // Then
        assertTrue(result);
        verify(gameRepository).existsById(1L);
        verify(gameRepository).deleteById(1L);
    }

    @Test
    void deleteGame_WhenGameDoesNotExist_ShouldReturnFalse() {
        // Given
        when(gameRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = gameService.deleteGame(999L);

        // Then
        assertFalse(result);
        verify(gameRepository).existsById(999L);
        verify(gameRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchGames_WithKeyword_ShouldReturnFilteredGames() {
        // Given
        List<Game> games = Arrays.asList(testGame);
        when(gameRepository.findByKeyword("Catan")).thenReturn(games);
        when(avisRepository.getAverageRatingByGameId(anyLong())).thenReturn(4.5);
        when(avisRepository.countApprovedByGameId(anyLong())).thenReturn(10L);
        when(inventoryRepository.findByGameId(anyLong())).thenReturn(Optional.empty());

        GameSearchDTO searchDTO = GameSearchDTO.builder()
                .keyword("Catan")
                .build();

        // When
        List<GameDTO> result = gameService.searchGames(searchDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Catan", result.get(0).getNom());
        verify(gameRepository).findByKeyword("Catan");
    }

    @Test
    void searchGames_WithPriceRange_ShouldReturnFilteredGames() {
        // Given
        List<Game> games = Arrays.asList(testGame);
        when(gameRepository.findByKeyword(anyString())).thenReturn(games);
        when(avisRepository.getAverageRatingByGameId(anyLong())).thenReturn(4.5);
        when(avisRepository.countApprovedByGameId(anyLong())).thenReturn(10L);
        when(inventoryRepository.findByGameId(anyLong())).thenReturn(Optional.empty());

        GameSearchDTO searchDTO = GameSearchDTO.builder()
                .keyword("Catan") // Ajout d'un mot-clé pour que la recherche fonctionne
                .minPrice(new BigDecimal("40.00"))
                .maxPrice(new BigDecimal("50.00"))
                .build();

        // When
        List<GameDTO> result = gameService.searchGames(searchDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(gameRepository).findByKeyword("Catan");
    }

    @Test
    void getGamesByCategory_ShouldReturnGamesInCategory() {
        // Given
        List<Game> games = Arrays.asList(testGame);
        when(gameRepository.findByCategoryId(1L)).thenReturn(games);
        when(avisRepository.getAverageRatingByGameId(anyLong())).thenReturn(4.5);
        when(avisRepository.countApprovedByGameId(anyLong())).thenReturn(10L);
        when(inventoryRepository.findByGameId(anyLong())).thenReturn(Optional.empty());

        // When
        List<GameDTO> result = gameService.getGamesByCategory(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCategoryId());
        verify(gameRepository).findByCategoryId(1L);
    }

    @Test
    void getAvailableGamesCount_ShouldReturnCorrectCount() {
        // Given
        when(gameRepository.countAvailableGames()).thenReturn(5L);

        // When
        long result = gameService.getAvailableGamesCount();

        // Then
        assertEquals(5L, result);
        verify(gameRepository).countAvailableGames();
    }
} 