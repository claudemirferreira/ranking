package br.com.setebit.ranking.application.dto;

import java.time.Instant;
import java.time.LocalDate;

public record AlunoResponse(
		Long id,
		Long turmaId,
		String nome,
		LocalDate dataNascimento,
		boolean ativo,
		Instant createdAt,
		Instant updatedAt
) {
}
