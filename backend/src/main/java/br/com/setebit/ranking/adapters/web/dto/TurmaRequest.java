package br.com.setebit.ranking.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;

public record TurmaRequest(
		@NotBlank String nome,
		String descricao
) {
}
