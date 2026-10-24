package br.com.setebit.ranking.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Aluno {

	private Long id;
	private Long turmaId;
	private Integer numero;
	private String nome;
	private LocalDate dataNascimento;
	private boolean ativo = true;
	private Instant createdAt;
	private Instant updatedAt;
}
