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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAlunoId() {
		return alunoId;
	}

	public void setAlunoId(Long alunoId) {
		this.alunoId = alunoId;
	}

	public Long getSabadoId() {
		return sabadoId;
	}

	public void setSabadoId(Long sabadoId) {
		this.sabadoId = sabadoId;
	}

	public Long getCriterioId() {
		return criterioId;
	}

	public void setCriterioId(Long criterioId) {
		this.criterioId = criterioId;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Instant getLancadoEm() {
		return lancadoEm;
	}

	public void setLancadoEm(Instant lancadoEm) {
		this.lancadoEm = lancadoEm;
	}

	public Instant getAtualizadoEm() {
		return atualizadoEm;
	}

	public void setAtualizadoEm(Instant atualizadoEm) {
		this.atualizadoEm = atualizadoEm;
	}
}
