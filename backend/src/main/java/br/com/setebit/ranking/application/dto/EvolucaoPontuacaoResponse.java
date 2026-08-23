package br.com.setebit.ranking.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record EvolucaoPontuacaoResponse(
		List<String> labels,
		List<AlunoSerieResponse> alunos
) {
	public record AlunoSerieResponse(
			Long alunoId,
			String nome,
			List<BigDecimal> pontosPorSabado,
			List<BigDecimal> acumulado
	) {
	}
}
