package br.com.setebit.ranking.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Nota {

	private Long id;
	private Long alunoId;
	private Long sabadoId;
	private Long criterioId;
	private BigDecimal valor;
	private String observacao;
	private Instant lancadoEm;
	private Instant atualizadoEm;

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
