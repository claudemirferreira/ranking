package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.EvolucaoPontuacaoResponse;
import br.com.setebit.ranking.application.port.in.GetEvolucaoPontuacaoUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.SabadoRepositoryPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.application.port.out.TrimestreRepositoryPort;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Sabado;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Trimestre;
import br.com.setebit.ranking.domain.service.EvolucaoPontuacaoCalculator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetEvolucaoPontuacaoUseCaseImpl implements GetEvolucaoPontuacaoUseCase {

	private final TrimestreRepositoryPort trimestreRepository;
	private final AlunoRepositoryPort alunoRepository;
	private final SabadoRepositoryPort sabadoRepository;
	private final NotaRepositoryPort notaRepository;
	private final EvolucaoPontuacaoCalculator calculator = new EvolucaoPontuacaoCalculator();

	public GetEvolucaoPontuacaoUseCaseImpl(
			TrimestreRepositoryPort trimestreRepository,
			AlunoRepositoryPort alunoRepository,
			SabadoRepositoryPort sabadoRepository,
			NotaRepositoryPort notaRepository) {
		this.trimestreRepository = trimestreRepository;
		this.alunoRepository = alunoRepository;
		this.sabadoRepository = sabadoRepository;
		this.notaRepository = notaRepository;
	}

	@Override
	public EvolucaoPontuacaoResponse execute(Long trimestreId) {
		Trimestre trimestre = trimestreRepository.findById(trimestreId)
				.orElseThrow(() -> new ResourceNotFoundException("Trimestre não encontrado: " + trimestreId));

		List<Aluno> alunos = alunoRepository.findActiveByTurmaId(trimestre.getTurmaId());
		List<Sabado> sabados = sabadoRepository.findByTrimestreId(trimestreId);
		List<Nota> notas = notaRepository.findByTrimestreId(trimestreId);

		EvolucaoPontuacaoCalculator.Evolucao evolucao = calculator.calculate(alunos, sabados, notas);
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
