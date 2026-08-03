package br.com.setebit.ranking.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record RankingItemResponse(
		int posicao,
		Long alunoId,
		Integer numero,
		String nome,
		BigDecimal total,
		List<CriterioTotalResponse> porCriterio
) {
	public record CriterioTotalResponse(Long criterioId, String nome, BigDecimal total) {
	}
}
