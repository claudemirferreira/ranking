package br.com.setebit.ranking.domain.service;

import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Nota;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Pure domain service responsible for ranking calculation (SRP / no framework).
 */
public class RankingCalculator {

	public List<RankingEntry> calculate(List<Aluno> alunosAtivos, List<Nota> notas, List<Criterio> criterios) {
		Map<Long, BigDecimal> totalPorAluno = new HashMap<>();
		Map<Long, Map<Long, BigDecimal>> porCriterio = new HashMap<>();

		for (Nota nota : notas) {
			totalPorAluno.merge(nota.getAlunoId(), nota.getValor(), BigDecimal::add);
			porCriterio
					.computeIfAbsent(nota.getAlunoId(), ignored -> new HashMap<>())
					.merge(nota.getCriterioId(), nota.getValor(), BigDecimal::add);
		}

		List<RankingEntry> entries = new ArrayList<>();
		for (Aluno aluno : alunosAtivos) {
			BigDecimal total = totalPorAluno.getOrDefault(aluno.getId(), BigDecimal.ZERO);
			Map<Long, BigDecimal> criteriosAluno = porCriterio.getOrDefault(aluno.getId(), Map.of());
			List<CriterioTotal> detalhe = criterios.stream()
					.sorted(Comparator.comparingInt(Criterio::getOrdem))
					.map(c -> new CriterioTotal(c.getId(), c.getNome(), criteriosAluno.getOrDefault(c.getId(), BigDecimal.ZERO)))
					.toList();
			entries.add(new RankingEntry(aluno.getId(), aluno.getNome(), total, detalhe));
		}

		entries.sort(Comparator
				.comparing(RankingEntry::total, Comparator.reverseOrder())
				.thenComparing(RankingEntry::nome, Comparator.nullsLast(String::compareToIgnoreCase)));

		List<RankingEntry> ranked = new ArrayList<>(entries.size());
		for (int i = 0; i < entries.size(); i++) {
			RankingEntry e = entries.get(i);
			ranked.add(new RankingEntry(i + 1, e.alunoId(), e.nome(), e.total(), e.porCriterio()));
		}
		return ranked;
	}

	public record CriterioTotal(Long criterioId, String nome, BigDecimal total) {
	}

	public record RankingEntry(
			int posicao,
			Long alunoId,
			String nome,
			BigDecimal total,
			List<CriterioTotal> porCriterio
	) {
		public RankingEntry(Long alunoId, String nome, BigDecimal total, List<CriterioTotal> porCriterio) {
			this(0, alunoId, nome, total, porCriterio);
		}

		public RankingEntry {
			Objects.requireNonNull(alunoId);
			Objects.requireNonNull(total);
			porCriterio = porCriterio == null ? List.of() : List.copyOf(porCriterio);
		}
	}
}
