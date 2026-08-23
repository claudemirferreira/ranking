package br.com.setebit.ranking.domain.service;

import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Nota;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EvolucaoPontuacaoCalculator {

	public Evolucao calculate(List<Aluno> alunosAtivos, List<Encontro> encontros, List<Nota> notas) {
		List<Encontro> ordered = encontros.stream()
				.sorted(Comparator.comparingInt(Encontro::getNumero))
				.toList();

		Map<Long, Map<Long, BigDecimal>> pontosPorAlunoEncontro = new HashMap<>();
		for (Nota nota : notas) {
			if (nota.getValor() == null) {
				continue;
			}
			pontosPorAlunoEncontro
					.computeIfAbsent(nota.getAlunoId(), ignored -> new HashMap<>())
					.merge(nota.getEncontroId(), nota.getValor(), BigDecimal::add);
		}

		List<AlunoSerie> series = new ArrayList<>();
		List<Aluno> orderedAlunos = alunosAtivos.stream()
				.sorted(Comparator.comparing(Aluno::getNome, Comparator.nullsLast(String::compareToIgnoreCase)))
				.toList();

		for (Aluno aluno : orderedAlunos) {
			Map<Long, BigDecimal> porEncontro = pontosPorAlunoEncontro.getOrDefault(aluno.getId(), Map.of());
			List<BigDecimal> pontosPorSabado = new ArrayList<>();
			List<BigDecimal> acumulado = new ArrayList<>();
			BigDecimal soma = BigDecimal.ZERO;
			for (Encontro encontro : ordered) {
				BigDecimal pontos = porEncontro.getOrDefault(encontro.getId(), BigDecimal.ZERO);
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
