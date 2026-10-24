package br.com.setebit.ranking.domain.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Sabado {

	private Long id;
	private Long trimestreId;
	private int numero;
	private LocalDate data;
	private String titulo;
}
