package br.com.setebit.ranking.application.dto;

import java.time.LocalDate;

public record EncontroResponse(
		Long id,
		Long trimestreId,
		int numero,
		LocalDate data,
		String titulo
) {
}
