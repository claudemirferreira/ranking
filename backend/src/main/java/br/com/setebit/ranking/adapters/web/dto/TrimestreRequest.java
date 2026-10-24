package br.com.setebit.ranking.adapters.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TrimestreRequest(
		@NotBlank String nome,
		@NotNull @Min(2000) @Max(2100) Integer ano,
		LocalDate dataInicio,
		LocalDate dataFim,
		Boolean ativo
) {
}
