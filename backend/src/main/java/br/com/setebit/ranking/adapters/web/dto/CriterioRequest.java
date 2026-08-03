package br.com.setebit.ranking.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record CriterioRequest(
		@NotBlank String nome,
		Integer ordem,
		BigDecimal pontuacaoMaxima
) {
}
