package br.com.setebit.ranking.domain.model;

import java.time.LocalDate;

public class Encontro {

	private Long id;
	private Long trimestreId;
	private int numero;
	private LocalDate data;
	private String titulo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTrimestreId() {
		return trimestreId;
	}

	public void setTrimestreId(Long trimestreId) {
		this.trimestreId = trimestreId;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
