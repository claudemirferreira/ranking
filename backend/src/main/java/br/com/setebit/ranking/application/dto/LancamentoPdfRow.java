package br.com.setebit.ranking.application.dto;

import java.math.BigDecimal;

public class LancamentoPdfRow {

	private final Integer posicao;
	private final String nome;
	private final BigDecimal total;
	private final String criterio;

	public LancamentoPdfRow(Integer posicao, String nome, BigDecimal total, String criterio) {
		this.posicao = posicao;
		this.nome = nome;
		this.total = total;
		this.criterio = criterio;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public String getNome() {
		return nome;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public String getCriterio() {
		return criterio;
	}
}
