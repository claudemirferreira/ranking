package br.com.setebit.ranking.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Nota;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class RankingCalculatorTest {

	private final RankingCalculator calculator = new RankingCalculator();

	@Test
	void shouldOrderByTotalDescendingAndNameOnTie() {
		Aluno a = aluno(1L, "Ana", true);
		Aluno b = aluno(2L, "Bruno", true);
		Aluno c = aluno(3L, "Carla", true);

		Criterio criterio = new Criterio();
		criterio.setId(10L);
		criterio.setNome("Presença");
		criterio.setOrdem(1);

		List<Nota> notas = List.of(
				nota(1L, 10L, "10"),
				nota(2L, 10L, "20"),
				nota(3L, 10L, "20"));

		var ranking = calculator.calculate(List.of(a, b, c), notas, List.of(criterio));

		assertEquals(3, ranking.size());
		assertEquals(1, ranking.get(0).posicao());
		assertEquals("Bruno", ranking.get(0).nome());
		assertEquals(0, new BigDecimal("20").compareTo(ranking.get(0).total()));
		assertEquals("Carla", ranking.get(1).nome());
		assertEquals("Ana", ranking.get(2).nome());
	}

	@Test
	void shouldIgnoreInactiveStudentsWhenNotPassed() {
		Aluno ativo = aluno(1L, "Ana", true);
		Nota nota = nota(1L, 10L, "5");
		Criterio criterio = new Criterio();
		criterio.setId(10L);
		criterio.setNome("A");
		criterio.setOrdem(1);

		var ranking = calculator.calculate(List.of(ativo), List.of(nota), List.of(criterio));
		assertEquals(1, ranking.size());
		assertEquals(0, new BigDecimal("5").compareTo(ranking.getFirst().total()));
	}

	private static Aluno aluno(Long id, String nome, boolean ativo) {
		Aluno aluno = new Aluno();
		aluno.setId(id);
		aluno.setNome(nome);
		aluno.setNumero(id.intValue());
		aluno.setAtivo(ativo);
		return aluno;
	}

	private static Nota nota(Long alunoId, Long criterioId, String valor) {
		Nota nota = new Nota();
		nota.setAlunoId(alunoId);
		nota.setEncontroId(1L);
		nota.setCriterioId(criterioId);
		nota.setValor(new BigDecimal(valor));
		return nota;
	}
}
