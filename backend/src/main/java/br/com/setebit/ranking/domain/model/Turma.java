package br.com.setebit.ranking.domain.model;

import java.time.Instant;
import lombok.Data;

@Data
public class Turma {

	private Long id;
	private String nome;
	private String descricao;
	private boolean ativo = true;
	private Instant createdAt;
	private Instant updatedAt;
	private Instant excluidoEm;

	public void softDelete() {
		this.ativo = false;
		this.excluidoEm = Instant.now();
		this.updatedAt = this.excluidoEm;
	}
}
