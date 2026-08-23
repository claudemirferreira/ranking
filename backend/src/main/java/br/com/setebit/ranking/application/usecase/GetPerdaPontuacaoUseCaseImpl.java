package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.PerdaPontuacaoResponse;
import br.com.setebit.ranking.application.port.in.GetPerdaPontuacaoUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.application.port.out.EncontroRepositoryPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.application.port.out.PeriodoRepositoryPort;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Periodo;
import br.com.setebit.ranking.domain.service.PerdaPontuacaoCalculator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetPerdaPontuacaoUseCaseImpl implements GetPerdaPontuacaoUseCase {

	private final PeriodoRepositoryPort periodoRepository;
	private final AlunoRepositoryPort alunoRepository;
	private final EncontroRepositoryPort encontroRepository;
	private final CriterioRepositoryPort criterioRepository;
	private final NotaRepositoryPort notaRepository;
	private final PerdaPontuacaoCalculator calculator = new PerdaPontuacaoCalculator();

	public GetPerdaPontuacaoUseCaseImpl(
			PeriodoRepositoryPort periodoRepository,
			AlunoRepositoryPort alunoRepository,
			EncontroRepositoryPort encontroRepository,
			CriterioRepositoryPort criterioRepository,
			NotaRepositoryPort notaRepository) {
		this.periodoRepository = periodoRepository;
		this.alunoRepository = alunoRepository;
		this.encontroRepository = encontroRepository;
		this.criterioRepository = criterioRepository;
		this.notaRepository = notaRepository;
	}

	@Override
	public PerdaPontuacaoResponse execute(Long periodoId) {
		Periodo periodo = periodoRepository.findById(periodoId)
				.orElseThrow(() -> new ResourceNotFoundException("Período não encontrado: " + periodoId));

		List<Aluno> alunos = alunoRepository.findActiveByTurmaId(periodo.getTurmaId());
		List<Encontro> encontros = encontroRepository.findByPeriodoId(periodoId);
		List<Criterio> criterios = criterioRepository.findByPeriodoId(periodoId);
		List<Nota> notas = notaRepository.findByPeriodoId(periodoId);

		return new PerdaPontuacaoResponse(
				calculator.calculate(alunos, encontros, criterios, notas).stream()
						.map(item -> new PerdaPontuacaoResponse.CriterioPerdaResponse(
								item.criterioId(),
								item.nome(),
								item.pontuacaoMaxima(),
								item.pontosPossiveis(),
								item.pontosObtidos(),
								item.pontosPerdidos(),
								item.percentualPerdido()))
						.toList());
	}
}
