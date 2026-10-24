package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.RankingItemResponse;
import br.com.setebit.ranking.application.port.in.GetRankingUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.application.port.out.TrimestreRepositoryPort;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Trimestre;
import br.com.setebit.ranking.domain.service.RankingCalculator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetRankingUseCaseImpl implements GetRankingUseCase {

	private final TrimestreRepositoryPort trimestreRepository;
	private final AlunoRepositoryPort alunoRepository;
	private final NotaRepositoryPort notaRepository;
	private final CriterioRepositoryPort criterioRepository;
	private final RankingCalculator rankingCalculator = new RankingCalculator();

	public GetRankingUseCaseImpl(
			TrimestreRepositoryPort trimestreRepository,
			AlunoRepositoryPort alunoRepository,
			NotaRepositoryPort notaRepository,
			CriterioRepositoryPort criterioRepository) {
		this.trimestreRepository = trimestreRepository;
		this.alunoRepository = alunoRepository;
		this.notaRepository = notaRepository;
		this.criterioRepository = criterioRepository;
	}

	@Override
	public List<RankingItemResponse> execute(Long trimestreId) {
		Trimestre trimestre = trimestreRepository.findById(trimestreId)
				.orElseThrow(() -> new ResourceNotFoundException("Trimestre não encontrado: " + trimestreId));

		List<Aluno> alunosAtivos = alunoRepository.findActiveByTurmaId(trimestre.getTurmaId());
		List<Nota> notas = notaRepository.findByTrimestreId(trimestreId);
		List<Criterio> criterios = criterioRepository.findByTrimestreId(trimestreId);

		return rankingCalculator.calculate(alunosAtivos, notas, criterios).stream()
				.map(e -> new RankingItemResponse(
						e.posicao(),
						e.alunoId(),
						e.numero(),
						e.nome(),
						e.total(),
						e.porCriterio().stream()
								.map(c -> new RankingItemResponse.CriterioTotalResponse(c.criterioId(), c.nome(), c.total()))
								.toList()))
				.toList();
	}
}
