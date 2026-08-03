package br.com.setebit.ranking.application.dto;

import java.time.Instant;

public record AlunoResponse(
		Long id,
		Long turmaId,
		Integer numero,
		String nome,
		boolean ativo,
		Instant createdAt,
		Instant updatedAt
) {
}
