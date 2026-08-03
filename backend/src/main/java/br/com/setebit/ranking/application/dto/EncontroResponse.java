package br.com.setebit.ranking.application.dto;

import java.time.LocalDate;

public record EncontroResponse(
		Long id,
		Long periodoId,
		int numero,
		LocalDate data,
		String titulo
) {
}
