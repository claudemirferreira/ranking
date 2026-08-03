package br.com.setebit.ranking.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record NotaResponse(
		Long id,
		Long alunoId,
		Long encontroId,
		Long criterioId,
		BigDecimal valor,
		String observacao,
		Instant lancadoEm,
		Instant atualizadoEm
) {
}
