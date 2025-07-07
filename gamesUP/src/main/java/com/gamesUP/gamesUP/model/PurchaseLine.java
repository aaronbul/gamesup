package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "purchase_lines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"purchase", "game"})
public class PurchaseLine {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    
    @Positive(message = "La quantité doit être positive")
    @Column(nullable = false)
    private int quantite = 1;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal prixRemise;
    
    public BigDecimal getPrixFinal() {
        return prixRemise != null ? prixRemise : prix;
    }
    
    public BigDecimal getTotalLigne() {
        return getPrixFinal().multiply(BigDecimal.valueOf(quantite));
    }
}
