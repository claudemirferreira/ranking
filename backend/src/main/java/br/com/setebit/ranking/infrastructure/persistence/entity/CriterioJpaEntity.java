package br.com.setebit.ranking.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "criterios")
public class CriterioJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "trimestre_id", nullable = false)
	private Long trimestreId;

	@Column(nullable = false, length = 120)
	private String nome;

	@Column(nullable = false)
	private int ordem;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal pontuacaoMaxima = BigDecimal.TEN;
}
