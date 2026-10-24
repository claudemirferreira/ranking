package br.com.setebit.ranking.application.dto;

import java.time.Instant;
import java.time.LocalDate;

public record TrimestreResponse(
		Long id,
		Long turmaId,
		String nome,
		int ano,
		LocalDate dataInicio,
		LocalDate dataFim,
		boolean ativo,
		Instant createdAt,
		Instant updatedAt
) {
	public String rotulo() {
		return nome + " · " + ano;
	}
}
