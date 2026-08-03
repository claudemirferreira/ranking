package br.com.setebit.ranking.application.dto;

import java.math.BigDecimal;

public record CriterioResponse(
		Long id,
		Long periodoId,
		String nome,
		int ordem,
		BigDecimal pontuacaoMaxima
) {
}
