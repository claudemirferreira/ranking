package br.com.setebit.ranking.domain.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Criterio {

	private Long id;
	private Long trimestreId;
	private String nome;
	private int ordem;
	private BigDecimal pontuacaoMaxima = BigDecimal.TEN;
}
