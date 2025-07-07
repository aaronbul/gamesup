package com.gamesUP.gamesUP.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"user", "lines"})
public class Purchase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(nullable = false)
	private LocalDateTime date = LocalDateTime.now();
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PurchaseStatus status = PurchaseStatus.PENDING;
	
	@Column(nullable = false)
	private boolean paid = false;
	
	@Column(nullable = false)
	private boolean delivered = false;
	
	@Column(nullable = false)
	private boolean archived = false;
	
	@OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<PurchaseLine> lines = new ArrayList<>();
	
	public enum PurchaseStatus {
		PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
	}
	
	    public void calculateTotal() {
        this.totalAmount = lines.stream()
                .map(PurchaseLine::getTotalLigne)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
