package br.com.setebit.ranking.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record PeriodoRequest(
		@NotBlank String nome,
		LocalDate dataInicio,
		LocalDate dataFim,
		Boolean ativo
) {
}
