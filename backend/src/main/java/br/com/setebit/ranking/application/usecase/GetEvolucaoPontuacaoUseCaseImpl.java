package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.EvolucaoPontuacaoResponse;
import br.com.setebit.ranking.application.port.in.GetEvolucaoPontuacaoUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.EncontroRepositoryPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.application.port.out.PeriodoRepositoryPort;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Periodo;
import br.com.setebit.ranking.domain.service.EvolucaoPontuacaoCalculator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetEvolucaoPontuacaoUseCaseImpl implements GetEvolucaoPontuacaoUseCase {

	private final PeriodoRepositoryPort periodoRepository;
	private final AlunoRepositoryPort alunoRepository;
	private final EncontroRepositoryPort encontroRepository;
	private final NotaRepositoryPort notaRepository;
	private final EvolucaoPontuacaoCalculator calculator = new EvolucaoPontuacaoCalculator();

	public GetEvolucaoPontuacaoUseCaseImpl(
			PeriodoRepositoryPort periodoRepository,
			AlunoRepositoryPort alunoRepository,
			EncontroRepositoryPort encontroRepository,
			NotaRepositoryPort notaRepository) {
		this.periodoRepository = periodoRepository;
		this.alunoRepository = alunoRepository;
		this.encontroRepository = encontroRepository;
		this.notaRepository = notaRepository;
	}

	@Override
	public EvolucaoPontuacaoResponse execute(Long periodoId) {
		Periodo periodo = periodoRepository.findById(periodoId)
				.orElseThrow(() -> new ResourceNotFoundException("Período não encontrado: " + periodoId));

		List<Aluno> alunos = alunoRepository.findActiveByTurmaId(periodo.getTurmaId());
		List<Encontro> encontros = encontroRepository.findByPeriodoId(periodoId);
		List<Nota> notas = notaRepository.findByPeriodoId(periodoId);

		EvolucaoPontuacaoCalculator.Evolucao evolucao = calculator.calculate(alunos, encontros, notas);
		return new EvolucaoPontuacaoResponse(
				evolucao.labels(),
				evolucao.alunos().stream()
						.map(s -> new EvolucaoPontuacaoResponse.AlunoSerieResponse(
								s.alunoId(),
								s.nome(),
								s.pontosPorSabado(),
								s.acumulado()))
						.toList());
	}
}
