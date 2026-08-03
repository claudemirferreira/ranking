package br.com.setebit.ranking.application.dto;

import java.math.BigDecimal;

public class RankingPdfRow {

	private final String nome;
	private final BigDecimal total;
	private final String criterio;

	public RankingPdfRow(String nome, BigDecimal total, String criterio) {
		this.nome = nome;
		this.total = total;
		this.criterio = criterio;
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
