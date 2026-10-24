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
import java.time.Instant;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(
		name = "trimestres",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_trimestre_turma_ano_nome",
				columnNames = {"turma_id", "ano", "nome"}))
public class TrimestreJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long turmaId;

	@Column(nullable = false, length = 120)
	private String nome;

	@Column(nullable = false)
	private int ano;

	private LocalDate dataInicio;

	private LocalDate dataFim;

	@Column(nullable = false)
	private boolean ativo = true;

	@Column(nullable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private Instant updatedAt;

	@PrePersist
	void onCreate() {
		Instant now = Instant.now();
		if (createdAt == null) {
			createdAt = now;
		}
		updatedAt = now;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = Instant.now();
	}
}
