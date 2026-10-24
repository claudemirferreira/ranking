package br.com.setebit.ranking.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record AlunoRequest(
		Integer numero,
		@NotBlank String nome,
		LocalDate dataNascimento
) {
}
