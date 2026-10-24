package br.com.setebit.ranking.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;

@Data
@Entity
@Table(
		name = "notas",
		uniqueConstraints = @UniqueConstraint(columnNames = {"alunoId", "sabadoId", "criterioId"}))
public class NotaJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long alunoId;

	@Column(name = "sabado_id", nullable = false)
	private Long sabadoId;

	@Column(nullable = false)
	private Long criterioId;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal valor;

	@Column(length = 500)
	private String observacao;

	@Column(nullable = false)
	private Instant lancadoEm;

	@Column(nullable = false)
	private Instant atualizadoEm;

	@PrePersist
	void onCreate() {
		Instant now = Instant.now();
		if (lancadoEm == null) {
			lancadoEm = now;
		}
		atualizadoEm = now;
	}

	@PreUpdate
	void onUpdate() {
		atualizadoEm = Instant.now();
	}
}
