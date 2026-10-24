package br.com.setebit.ranking.domain.service;

import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Sabado;
import br.com.setebit.ranking.domain.model.Nota;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EvolucaoPontuacaoCalculator {

	public Evolucao calculate(List<Aluno> alunosAtivos, List<Sabado> sabados, List<Nota> notas) {
		List<Sabado> ordered = sabados.stream()
				.sorted(Comparator.comparingInt(Sabado::getNumero))
				.toList();

		Map<Long, Map<Long, BigDecimal>> pontosPorAlunoSabado = new HashMap<>();
		for (Nota nota : notas) {
			if (nota.getValor() == null) {
				continue;
			}
			pontosPorAlunoSabado
					.computeIfAbsent(nota.getAlunoId(), ignored -> new HashMap<>())
					.merge(nota.getSabadoId(), nota.getValor(), BigDecimal::add);
		}

		List<AlunoSerie> series = new ArrayList<>();
		List<Aluno> orderedAlunos = alunosAtivos.stream()
				.sorted(Comparator.comparing(Aluno::getNome, Comparator.nullsLast(String::compareToIgnoreCase)))
				.toList();

		for (Aluno aluno : orderedAlunos) {
			Map<Long, BigDecimal> porSabado = pontosPorAlunoSabado.getOrDefault(aluno.getId(), Map.of());
			List<BigDecimal> pontosPorSabado = new ArrayList<>();
			List<BigDecimal> acumulado = new ArrayList<>();
			BigDecimal soma = BigDecimal.ZERO;
			for (Sabado sabado : ordered) {
				BigDecimal pontos = porSabado.getOrDefault(sabado.getId(), BigDecimal.ZERO);
				pontosPorSabado.add(pontos);
				soma = soma.add(pontos);
				acumulado.add(soma);
			}
			series.add(new AlunoSerie(aluno.getId(), aluno.getNome(), pontosPorSabado, acumulado));
		}

		List<String> labels = ordered.stream()
				.map(e -> e.getTitulo() == null || e.getTitulo().isBlank() ? "Sábado " + e.getNumero() : e.getTitulo())
				.toList();

		return new Evolucao(labels, series);
	}

	public record Evolucao(List<String> labels, List<AlunoSerie> alunos) {
		public Evolucao {
			labels = labels == null ? List.of() : List.copyOf(labels);
			alunos = alunos == null ? List.of() : List.copyOf(alunos);
		}
	}

	public record AlunoSerie(
			Long alunoId,
			String nome,
			List<BigDecimal> pontosPorSabado,
			List<BigDecimal> acumulado
	) {
		public AlunoSerie {
			Objects.requireNonNull(alunoId);
			pontosPorSabado = pontosPorSabado == null ? List.of() : List.copyOf(pontosPorSabado);
			acumulado = acumulado == null ? List.of() : List.copyOf(acumulado);
		}
	}
}
