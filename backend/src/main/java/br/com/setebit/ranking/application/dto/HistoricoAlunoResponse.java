package br.com.setebit.ranking.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record HistoricoAlunoResponse(
		Long alunoId,
		String nome,
		Long trimestreId,
		BigDecimal total,
		List<NotaResponse> notas
) {
}
