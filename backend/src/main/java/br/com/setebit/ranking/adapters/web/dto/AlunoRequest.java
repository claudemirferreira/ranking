package br.com.setebit.ranking.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AlunoRequest(
		@NotBlank String nome,
		@NotNull LocalDate dataNascimento
) {
}
