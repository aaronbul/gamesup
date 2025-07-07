package com.gamesUP.gamesUP.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    
    private Long id;
    private String type;
    private String description;
    // On n'inclut PAS la liste des jeux pour éviter la boucle infinie
} 