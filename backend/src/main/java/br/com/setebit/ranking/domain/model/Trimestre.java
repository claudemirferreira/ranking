package br.com.setebit.ranking.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Trimestre {

	private Long id;
	private Long turmaId;
	private String nome;
	private int ano;
	private LocalDate dataInicio;
	private LocalDate dataFim;
	private boolean ativo = true;
	private Instant createdAt;
	private Instant updatedAt;
}
