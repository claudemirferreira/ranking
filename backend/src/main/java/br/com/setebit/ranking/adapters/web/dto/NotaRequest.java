package br.com.setebit.ranking.adapters.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record NotaRequest(
		@NotNull Long alunoId,
		@NotNull Long sabadoId,
		@NotNull Long criterioId,
		@NotNull @DecimalMin("0.0") BigDecimal valor,
		String observacao
) {
}
