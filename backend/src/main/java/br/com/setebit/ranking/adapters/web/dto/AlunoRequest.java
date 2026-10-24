package br.com.setebit.ranking.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AlunoRequest(
		Integer numero,
		@NotBlank String nome
) {
}
