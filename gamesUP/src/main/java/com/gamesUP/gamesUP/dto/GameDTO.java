package com.gamesUP.gamesUP.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDTO {
    
    private Long id;
    
    @NotBlank(message = "Le nom du jeu est obligatoire")
    private String nom;
    
    private String description;
    
    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private BigDecimal prix;
    
    private int numEdition = 1;
    
    private int ageMinimum = 0;
    
    private int nombreJoueursMin = 1;
    
    private int nombreJoueursMax = 4;
    
    private int dureePartieMinutes = 60;
    
    private boolean disponible = true;
    
    private Long categoryId;
    
    private String categoryType;
    
    private Long publisherId;
    
    private String publisherName;
    
    private Set<Long> authorIds;
    
    private Set<String> authorNames;
    
    private Double averageRating;
    
    private Long numberOfReviews;
    
    private Integer stock;
} 