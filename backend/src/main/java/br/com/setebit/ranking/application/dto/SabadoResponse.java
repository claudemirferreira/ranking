package br.com.setebit.ranking.application.dto;

import java.time.LocalDate;

public record SabadoResponse(
		Long id,
		Long trimestreId,
		int numero,
		LocalDate data,
		String titulo
) {
}
