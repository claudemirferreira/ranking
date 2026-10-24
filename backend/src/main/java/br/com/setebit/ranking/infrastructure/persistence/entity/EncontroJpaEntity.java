package br.com.setebit.ranking.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(
		name = "encontros",
		uniqueConstraints = @UniqueConstraint(name = "uk_trimestre_numero", columnNames = {"trimestre_id", "numero"}))
public class EncontroJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "trimestre_id", nullable = false)
	private Long trimestreId;

	@Column(nullable = false)
	private int numero;

	private LocalDate data;

	@Column(nullable = false, length = 120)
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
