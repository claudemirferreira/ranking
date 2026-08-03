package br.com.setebit.ranking.domain.model;

import java.time.Instant;

public class Turma {

	private Long id;
	private String nome;
	private String descricao;
	private boolean ativo = true;
	private Instant createdAt;
	private Instant updatedAt;
	private Instant excluidoEm;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Instant getExcluidoEm() {
		return excluidoEm;
	}

	public void setExcluidoEm(Instant excluidoEm) {
		this.excluidoEm = excluidoEm;
	}

	public void softDelete() {
		this.ativo = false;
		this.excluidoEm = Instant.now();
		this.updatedAt = this.excluidoEm;
	}
}
