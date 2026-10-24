package br.com.setebit.ranking.application.dto;

import java.time.Instant;
import java.time.LocalDate;

public record TrimestreResponse(
		Long id,
		Long turmaId,
		String nome,
		LocalDate dataInicio,
		LocalDate dataFim,
		boolean ativo,
		Instant createdAt,
		Instant updatedAt
) {
}
