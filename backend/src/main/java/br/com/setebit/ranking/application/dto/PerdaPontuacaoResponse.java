package br.com.setebit.ranking.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record PerdaPontuacaoResponse(List<CriterioPerdaResponse> itens) {

	public record CriterioPerdaResponse(
			Long criterioId,
			String nome,
			BigDecimal pontuacaoMaxima,
			BigDecimal pontosPossiveis,
			BigDecimal pontosObtidos,
			BigDecimal pontosPerdidos,
			BigDecimal percentualPerdido
	) {
	}
}
