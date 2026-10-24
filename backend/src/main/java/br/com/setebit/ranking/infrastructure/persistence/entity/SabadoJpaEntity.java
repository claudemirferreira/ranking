package br.com.setebit.ranking.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(
		name = "sabados",
		uniqueConstraints = @UniqueConstraint(name = "uk_trimestre_numero", columnNames = {"trimestre_id", "numero"}))
public class SabadoJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "trimestre_id", nullable = false)
	private Long trimestreId;

	@Column(nullable = false)
	private int numero;

	private LocalDate data;

	@Column(nullable = false, length = 120)
	private String titulo;
}
