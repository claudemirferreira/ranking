package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.PerdaPontuacaoResponse;
import br.com.setebit.ranking.application.port.in.GetPerdaPontuacaoUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.application.port.out.EncontroRepositoryPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.application.port.out.TrimestreRepositoryPort;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Trimestre;
import br.com.setebit.ranking.domain.service.PerdaPontuacaoCalculator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetPerdaPontuacaoUseCaseImpl implements GetPerdaPontuacaoUseCase {

	private final TrimestreRepositoryPort trimestreRepository;
	private final AlunoRepositoryPort alunoRepository;
	private final EncontroRepositoryPort encontroRepository;
	private final CriterioRepositoryPort criterioRepository;
	private final NotaRepositoryPort notaRepository;
	private final PerdaPontuacaoCalculator calculator = new PerdaPontuacaoCalculator();

	public GetPerdaPontuacaoUseCaseImpl(
			TrimestreRepositoryPort trimestreRepository,
			AlunoRepositoryPort alunoRepository,
			EncontroRepositoryPort encontroRepository,
			CriterioRepositoryPort criterioRepository,
			NotaRepositoryPort notaRepository) {
		this.trimestreRepository = trimestreRepository;
		this.alunoRepository = alunoRepository;
		this.encontroRepository = encontroRepository;
		this.criterioRepository = criterioRepository;
		this.notaRepository = notaRepository;
	}

	@Override
	public PerdaPontuacaoResponse execute(Long trimestreId) {
		Trimestre trimestre = trimestreRepository.findById(trimestreId)
				.orElseThrow(() -> new ResourceNotFoundException("Trimestre não encontrado: " + trimestreId));

		List<Aluno> alunos = alunoRepository.findActiveByTurmaId(trimestre.getTurmaId());
		List<Encontro> encontros = encontroRepository.findByTrimestreId(trimestreId);
		List<Criterio> criterios = criterioRepository.findByTrimestreId(trimestreId);
		List<Nota> notas = notaRepository.findByTrimestreId(trimestreId);

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
