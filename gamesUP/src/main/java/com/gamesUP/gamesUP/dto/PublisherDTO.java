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
public class PublisherDTO {
    
    private Long id;
    private String name;
    private String description;
    private String website;
    private String country;
    // On n'inclut PAS la liste des jeux pour éviter la boucle infinie
} 