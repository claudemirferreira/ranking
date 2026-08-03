package br.com.setebit.ranking.domain.model;

import java.math.BigDecimal;

public class Criterio {

	private Long id;
	private Long periodoId;
	private String nome;
	private int ordem;
	private BigDecimal pontuacaoMaxima = BigDecimal.TEN;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPeriodoId() {
		return periodoId;
	}

	public void setPeriodoId(Long periodoId) {
		this.periodoId = periodoId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public BigDecimal getPontuacaoMaxima() {
		return pontuacaoMaxima;
	}

	public void setPontuacaoMaxima(BigDecimal pontuacaoMaxima) {
		this.pontuacaoMaxima = pontuacaoMaxima;
	}
}
