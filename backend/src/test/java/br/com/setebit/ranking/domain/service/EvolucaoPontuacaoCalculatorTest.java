package br.com.setebit.ranking.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Nota;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class EvolucaoPontuacaoCalculatorTest {

	private final EvolucaoPontuacaoCalculator calculator = new EvolucaoPontuacaoCalculator();

	@Test
	void shouldSumScoresPerSaturdayAndAccumulate() {
		Aluno ana = aluno(1L, "Ana");
		Aluno bruno = aluno(2L, "Bruno");
		Encontro sabado1 = encontro(10L, 1);
		Encontro sabado2 = encontro(11L, 2);

		List<Nota> notas = List.of(
				nota(1L, 10L, "10"),
				nota(1L, 10L, "5"),
				nota(1L, 11L, "8"),
				nota(2L, 11L, "20"));

		var evolucao = calculator.calculate(List.of(ana, bruno), List.of(sabado2, sabado1), notas);

		assertEquals(List.of("Sábado 1", "Sábado 2"), evolucao.labels());
		assertEquals(2, evolucao.alunos().size());
		assertEquals("Ana", evolucao.alunos().get(0).nome());
		assertEquals(0, new BigDecimal("15").compareTo(evolucao.alunos().get(0).pontosPorSabado().get(0)));
		assertEquals(0, new BigDecimal("8").compareTo(evolucao.alunos().get(0).pontosPorSabado().get(1)));
		assertEquals(0, new BigDecimal("23").compareTo(evolucao.alunos().get(0).acumulado().get(1)));
		assertEquals("Bruno", evolucao.alunos().get(1).nome());
		assertEquals(0, BigDecimal.ZERO.compareTo(evolucao.alunos().get(1).pontosPorSabado().get(0)));
		assertEquals(0, new BigDecimal("20").compareTo(evolucao.alunos().get(1).acumulado().get(1)));
	}

	private static Aluno aluno(Long id, String nome) {
		Aluno aluno = new Aluno();
		aluno.setId(id);
		aluno.setNome(nome);
		aluno.setAtivo(true);
		return aluno;
	}

	private static Encontro encontro(Long id, int numero) {
		Encontro encontro = new Encontro();
		encontro.setId(id);
		encontro.setNumero(numero);
		encontro.setTitulo("Sábado " + numero);
		return encontro;
	}

	private static Nota nota(Long alunoId, Long encontroId, String valor) {
		Nota nota = new Nota();
		nota.setAlunoId(alunoId);
		nota.setEncontroId(encontroId);
		nota.setCriterioId(1L);
		nota.setValor(new BigDecimal(valor));
		return nota;
	}
}
