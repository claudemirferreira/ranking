package br.com.setebit.ranking.domain.service;

import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Nota;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PerdaPontuacaoCalculator {

	public List<CriterioPerda> calculate(
			List<Aluno> alunosAtivos,
			List<Encontro> encontros,
			List<Criterio> criterios,
			List<Nota> notas) {
		if (alunosAtivos.isEmpty() || encontros.isEmpty() || criterios.isEmpty()) {
			return List.of();
		}

		Set<Long> alunoIds = alunosAtivos.stream().map(Aluno::getId).collect(Collectors.toSet());
		Set<Long> encontroIds = encontros.stream().map(Encontro::getId).collect(Collectors.toSet());
		int lancamentos = alunosAtivos.size() * encontros.size();

		Map<String, BigDecimal> obtidoPorNota = new HashMap<>();
		for (Nota nota : notas) {
			if (nota.getValor() == null
					|| !alunoIds.contains(nota.getAlunoId())
					|| !encontroIds.contains(nota.getEncontroId())) {
				continue;
			}
			String chave = nota.getAlunoId() + ":" + nota.getEncontroId() + ":" + nota.getCriterioId();
			obtidoPorNota.merge(chave, nota.getValor(), BigDecimal::add);
		}

		List<CriterioPerda> itens = new ArrayList<>();
		for (Criterio criterio : criterios) {
			BigDecimal maximo = criterio.getPontuacaoMaxima() == null ? BigDecimal.TEN : criterio.getPontuacaoMaxima();
			BigDecimal possiveis = maximo.multiply(BigDecimal.valueOf(lancamentos));
			BigDecimal obtidos = BigDecimal.ZERO;
			for (Aluno aluno : alunosAtivos) {
				for (Encontro encontro : encontros) {
					String chave = aluno.getId() + ":" + encontro.getId() + ":" + criterio.getId();
					BigDecimal valor = obtidoPorNota.getOrDefault(chave, BigDecimal.ZERO);
					if (valor.compareTo(maximo) > 0) {
						valor = maximo;
					}
					obtidos = obtidos.add(valor);
				}
			}
			BigDecimal perdidos = possiveis.subtract(obtidos);
			if (perdidos.signum() < 0) {
				perdidos = BigDecimal.ZERO;
			}
			BigDecimal percentual = possiveis.signum() == 0
					? BigDecimal.ZERO
					: perdidos.multiply(BigDecimal.valueOf(100)).divide(possiveis, 1, RoundingMode.HALF_UP);
			itens.add(new CriterioPerda(
					criterio.getId(),
					criterio.getNome(),
					maximo,
					possiveis,
					obtidos,
					perdidos,
					percentual));
		}

		itens.sort(Comparator
				.comparing(CriterioPerda::pontosPerdidos).reversed()
				.thenComparing(CriterioPerda::nome, Comparator.nullsLast(String::compareToIgnoreCase)));
		return List.copyOf(itens);
	}

	public record CriterioPerda(
			Long criterioId,
			String nome,
			BigDecimal pontuacaoMaxima,
			BigDecimal pontosPossiveis,
			BigDecimal pontosObtidos,
			BigDecimal pontosPerdidos,
			BigDecimal percentualPerdido
	) {
		public CriterioPerda {
			Objects.requireNonNull(criterioId);
			pontosPerdidos = pontosPerdidos == null ? BigDecimal.ZERO : pontosPerdidos;
			percentualPerdido = percentualPerdido == null ? BigDecimal.ZERO : percentualPerdido;
		}
	}
}
