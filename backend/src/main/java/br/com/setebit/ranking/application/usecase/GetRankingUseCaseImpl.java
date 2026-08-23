package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.RankingItemResponse;
import br.com.setebit.ranking.application.port.in.GetRankingUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.application.port.out.PeriodoRepositoryPort;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Periodo;
import br.com.setebit.ranking.domain.service.RankingCalculator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetRankingUseCaseImpl implements GetRankingUseCase {

	private final PeriodoRepositoryPort periodoRepository;
	private final AlunoRepositoryPort alunoRepository;
	private final NotaRepositoryPort notaRepository;
	private final CriterioRepositoryPort criterioRepository;
	private final RankingCalculator rankingCalculator = new RankingCalculator();

	public GetRankingUseCaseImpl(
			PeriodoRepositoryPort periodoRepository,
			AlunoRepositoryPort alunoRepository,
			NotaRepositoryPort notaRepository,
			CriterioRepositoryPort criterioRepository) {
		this.periodoRepository = periodoRepository;
		this.alunoRepository = alunoRepository;
		this.notaRepository = notaRepository;
		this.criterioRepository = criterioRepository;
	}

	@Override
	public List<RankingItemResponse> execute(Long periodoId) {
		Periodo periodo = periodoRepository.findById(periodoId)
				.orElseThrow(() -> new ResourceNotFoundException("Período não encontrado: " + periodoId));

		List<Aluno> alunosAtivos = alunoRepository.findActiveByTurmaId(periodo.getTurmaId());
		List<Nota> notas = notaRepository.findByPeriodoId(periodoId);
		List<Criterio> criterios = criterioRepository.findByPeriodoId(periodoId);

		return rankingCalculator.calculate(alunosAtivos, notas, criterios).stream()
				.map(e -> new RankingItemResponse(
						e.posicao(),
						e.alunoId(),
						e.nome(),
						e.total(),
						e.porCriterio().stream()
								.map(c -> new RankingItemResponse.CriterioTotalResponse(c.criterioId(), c.nome(), c.total()))
								.toList()))
				.toList();
	}
}
