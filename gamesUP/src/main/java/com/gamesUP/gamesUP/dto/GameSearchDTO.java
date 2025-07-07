package com.gamesUP.gamesUP.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSearchDTO {
    
    private String keyword;
    
    private Long categoryId;
    
    private Long publisherId;
    
    private String authorName;
    
    private BigDecimal minPrice;
    
    private BigDecimal maxPrice;
    
    private Integer minAge;
    
    private Integer maxAge;
    
    private Integer minPlayers;
    
    private Integer maxPlayers;
    
    private Integer maxDuration;
    
    private Boolean disponible;
    
    private String sortBy = "nom"; // nom, prix, date
    
    private String sortDirection = "ASC"; // ASC, DESC
    
    private Integer page = 0;
    
    private Integer size = 20;
} 