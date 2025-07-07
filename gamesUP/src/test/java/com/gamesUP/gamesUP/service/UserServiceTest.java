package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.dto.UserDTO;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repository.UserRepository;
import com.gamesUP.gamesUP.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PurchaseRepository purchaseRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@email.com")
                .password("encodedPassword")
                .role(User.UserRole.CLIENT)
                .active(true)
                .build();

        testUserDTO = UserDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@email.com")
                .password("password123")
                .role(User.UserRole.CLIENT)
                .active(true)
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        when(purchaseRepository.countByUserId(anyLong())).thenReturn(5L);
        when(purchaseRepository.getTotalSpentByUser(anyLong())).thenReturn(150.0);

        // When
        List<UserDTO> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("jean.dupont@email.com", result.get(0).getEmail());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(purchaseRepository.countByUserId(1L)).thenReturn(5L);
        when(purchaseRepository.getTotalSpentByUser(1L)).thenReturn(150.0);

        // When
        Optional<UserDTO> result = userService.getUserById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("jean.dupont@email.com", result.get().getEmail());
        assertEquals(5L, result.get().getNumberOfPurchases());
        assertEquals(150.0, result.get().getTotalSpent());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<UserDTO> result = userService.getUserById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findById(999L);
    }

    @Test
    void getUserByEmail_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByEmail("jean.dupont@email.com")).thenReturn(Optional.of(testUser));
        when(purchaseRepository.countByUserId(1L)).thenReturn(5L);
        when(purchaseRepository.getTotalSpentByUser(1L)).thenReturn(150.0);

        // When
        Optional<UserDTO> result = userService.getUserByEmail("jean.dupont@email.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("jean.dupont@email.com", result.get().getEmail());
        verify(userRepository).findByEmail("jean.dupont@email.com");
    }

    @Test
    void createUser_ShouldCreateAndReturnUser() {
        // Given
        when(userRepository.existsByEmail("jean.dupont@email.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(purchaseRepository.countByUserId(1L)).thenReturn(0L);
        when(purchaseRepository.getTotalSpentByUser(1L)).thenReturn(0.0);

        // When
        UserDTO result = userService.createUser(testUserDTO);

        // Then
        assertNotNull(result);
        assertEquals("jean.dupont@email.com", result.getEmail());
        verify(userRepository).existsByEmail("jean.dupont@email.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WhenEmailExists_ShouldThrowException() {
        // Given
        when(userRepository.existsByEmail("jean.dupont@email.com")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.createUser(testUserDTO));
        verify(userRepository).existsByEmail("jean.dupont@email.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateAndReturnUser() {
        // Given
        User existingUser = User.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@email.com")
                .password("oldEncodedPassword")
                .role(User.UserRole.CLIENT)
                .active(true)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // When
        Optional<UserDTO> result = userService.updateUser(1L, testUserDTO);

        // Then
        assertTrue(result.isPresent());
        assertEquals("jean.dupont@email.com", result.get().getEmail());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<UserDTO> result = userService.updateUser(999L, testUserDTO);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnTrue() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = userService.deleteUser(1L);

        // Then
        assertTrue(result);
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Given
        when(userRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = userService.deleteUser(999L);

        // Then
        assertFalse(result);
        verify(userRepository).existsById(999L);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void deactivateUser_WhenUserExists_ShouldReturnTrue() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        boolean result = userService.deactivateUser(1L);

        // Then
        assertTrue(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void activateUser_WhenUserExists_ShouldReturnTrue() {
        // Given
        User inactiveUser = User.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@email.com")
                .password("encodedPassword")
                .role(User.UserRole.CLIENT)
                .active(false)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(inactiveUser));
        when(userRepository.save(any(User.class))).thenReturn(inactiveUser);

        // When
        boolean result = userService.activateUser(1L);

        // Then
        assertTrue(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changeUserRole_WhenUserExists_ShouldReturnTrue() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        boolean result = userService.changeUserRole(1L, User.UserRole.ADMIN);

        // Then
        assertTrue(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void existsByEmail_ShouldReturnCorrectValue() {
        // Given
        when(userRepository.existsByEmail("jean.dupont@email.com")).thenReturn(true);

        // When
        boolean result = userService.existsByEmail("jean.dupont@email.com");

        // Then
        assertTrue(result);
        verify(userRepository).existsByEmail("jean.dupont@email.com");
    }

    @Test
    void getUsersByRole_ShouldReturnUsersWithRole() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findByRole(User.UserRole.CLIENT)).thenReturn(users);
        when(purchaseRepository.countByUserId(anyLong())).thenReturn(5L);
        when(purchaseRepository.getTotalSpentByUser(anyLong())).thenReturn(150.0);

        // When
        List<UserDTO> result = userService.getUsersByRole(User.UserRole.CLIENT);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(User.UserRole.CLIENT, result.get(0).getRole());
        verify(userRepository).findByRole(User.UserRole.CLIENT);
    }

    @Test
    void getActiveUsers_ShouldReturnOnlyActiveUsers() {
        // Given
        List<User> activeUsers = Arrays.asList(testUser);
        when(userRepository.findByActive(true)).thenReturn(activeUsers);
        when(purchaseRepository.countByUserId(anyLong())).thenReturn(5L);
        when(purchaseRepository.getTotalSpentByUser(anyLong())).thenReturn(150.0);

        // When
        List<UserDTO> result = userService.getActiveUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
        verify(userRepository).findByActive(true);
    }

    @Test
    void searchUsers_ShouldReturnFilteredUsers() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findByKeyword("Dupont")).thenReturn(users);
        when(purchaseRepository.countByUserId(anyLong())).thenReturn(5L);
        when(purchaseRepository.getTotalSpentByUser(anyLong())).thenReturn(150.0);

        // When
        List<UserDTO> result = userService.searchUsers("Dupont");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dupont", result.get(0).getNom());
        verify(userRepository).findByKeyword("Dupont");
    }
} 