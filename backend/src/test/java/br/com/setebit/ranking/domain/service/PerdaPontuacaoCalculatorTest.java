package br.com.setebit.ranking.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Nota;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class PerdaPontuacaoCalculatorTest {

	private final PerdaPontuacaoCalculator calculator = new PerdaPontuacaoCalculator();

	@Test
	void shouldRankCriteriaByLostPoints() {
		Aluno aluno = aluno(1L, "Ana");
		Encontro encontro = encontro(10L, 1);
		Criterio presenca = criterio(1L, "Presença", "10");
		Criterio oracao = criterio(2L, "Oração", "10");

		List<Nota> notas = List.of(
				nota(1L, 10L, 1L, "10"),
				nota(1L, 10L, 2L, "4"));

		var itens = calculator.calculate(List.of(aluno), List.of(encontro), List.of(presenca, oracao), notas);

		assertEquals(2, itens.size());
		assertEquals("Oração", itens.get(0).nome());
		assertEquals(0, new BigDecimal("6").compareTo(itens.get(0).pontosPerdidos()));
		assertEquals("Presença", itens.get(1).nome());
		assertEquals(0, BigDecimal.ZERO.compareTo(itens.get(1).pontosPerdidos()));
	}

	@Test
	void shouldCountMissingNotesAsFullLoss() {
		Aluno aluno = aluno(1L, "Ana");
		Encontro encontro = encontro(10L, 1);
		Criterio presenca = criterio(1L, "Presença", "10");

		var itens = calculator.calculate(List.of(aluno), List.of(encontro), List.of(presenca), List.of());

		assertEquals(1, itens.size());
		assertEquals(0, new BigDecimal("10").compareTo(itens.getFirst().pontosPerdidos()));
		assertEquals(0, new BigDecimal("100.0").compareTo(itens.getFirst().percentualPerdido()));
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
		return encontro;
	}

	private static Criterio criterio(Long id, String nome, String maximo) {
		Criterio criterio = new Criterio();
		criterio.setId(id);
		criterio.setNome(nome);
		criterio.setPontuacaoMaxima(new BigDecimal(maximo));
		return criterio;
	}

	private static Nota nota(Long alunoId, Long encontroId, Long criterioId, String valor) {
		Nota nota = new Nota();
		nota.setAlunoId(alunoId);
		nota.setEncontroId(encontroId);
		nota.setCriterioId(criterioId);
		nota.setValor(new BigDecimal(valor));
		return nota;
	}
}
