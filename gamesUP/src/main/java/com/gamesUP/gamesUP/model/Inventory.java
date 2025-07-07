package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"game"})
public class Inventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_id", nullable = false, unique = true)
	private Game game;
	
	@Min(value = 0, message = "Le stock ne peut pas être négatif")
	@Column(nullable = false)
	private Integer stock = 0;
	
	@Min(value = 0, message = "Le stock minimum ne peut pas être négatif")
	@Column(nullable = false)
	private Integer stockMinimum = 5;
	
	@Column(nullable = false)
	private boolean disponible = true;
	
	public boolean isEnRupture() {
		return stock <= 0;
	}
	
	public boolean isStockFaible() {
		return stock <= stockMinimum;
	}
	
	public void decrementerStock(int quantite) {
		if (this.stock >= quantite) {
			this.stock -= quantite;
			if (this.stock == 0) {
				this.disponible = false;
			}
		} else {
			throw new IllegalStateException("Stock insuffisant");
		}
	}
	
	public void incrementerStock(int quantite) {
		this.stock += quantite;
		if (this.stock > 0) {
			this.disponible = true;
		}
	}
}
