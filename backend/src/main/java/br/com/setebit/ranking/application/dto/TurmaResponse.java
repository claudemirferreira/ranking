package br.com.setebit.ranking.application.dto;

import java.time.Instant;

public record TurmaResponse(
		Long id,
		String nome,
		String descricao,
		boolean ativo,
		Instant createdAt,
		Instant updatedAt
) {
}
