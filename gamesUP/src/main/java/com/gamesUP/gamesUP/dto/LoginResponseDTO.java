package com.gamesUP.gamesUP.dto;

import com.gamesUP.gamesUP.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    
    private String token;
    private Long userId;
    private String email;
    private String nom;
    private String prenom;
    private User.UserRole role;
    private String message;
} 