package br.com.setebit.ranking.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;

@Data
public class Nota {

	private Long id;
	private Long alunoId;
	private Long sabadoId;
	private Long criterioId;
	private BigDecimal valor;
	private String observacao;
	private Instant lancadoEm;
	private Instant atualizadoEm;
}
