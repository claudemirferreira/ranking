package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.LancamentoPdfRow;
import br.com.setebit.ranking.application.dto.TrimestreResponse;
import br.com.setebit.ranking.application.dto.TurmaResponse;
import br.com.setebit.ranking.application.port.in.ExportLancamentoPdfUseCase;
import br.com.setebit.ranking.application.port.in.ManageTrimestreUseCase;
import br.com.setebit.ranking.application.port.in.ManageTurmaUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.application.port.out.SabadoRepositoryPort;
import br.com.setebit.ranking.application.port.out.LancamentoPdfReportPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.domain.exception.BusinessRuleException;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Sabado;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.service.RankingCalculator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ExportLancamentoPdfUseCaseImpl implements ExportLancamentoPdfUseCase {

	private final ManageTrimestreUseCase manageTrimestreUseCase;
	private final ManageTurmaUseCase manageTurmaUseCase;
	private final SabadoRepositoryPort sabadoRepository;
	private final AlunoRepositoryPort alunoRepository;
	private final NotaRepositoryPort notaRepository;
	private final CriterioRepositoryPort criterioRepository;
	private final LancamentoPdfReportPort lancamentoPdfReportPort;
	private final RankingCalculator rankingCalculator = new RankingCalculator();

	public ExportLancamentoPdfUseCaseImpl(
			ManageTrimestreUseCase manageTrimestreUseCase,
			ManageTurmaUseCase manageTurmaUseCase,
			SabadoRepositoryPort sabadoRepository,
			AlunoRepositoryPort alunoRepository,
			NotaRepositoryPort notaRepository,
			CriterioRepositoryPort criterioRepository,
			LancamentoPdfReportPort lancamentoPdfReportPort) {
		this.manageTrimestreUseCase = manageTrimestreUseCase;
		this.manageTurmaUseCase = manageTurmaUseCase;
		this.sabadoRepository = sabadoRepository;
		this.alunoRepository = alunoRepository;
		this.notaRepository = notaRepository;
		this.criterioRepository = criterioRepository;
		this.lancamentoPdfReportPort = lancamentoPdfReportPort;
	}

	@Override
	public byte[] execute(Long trimestreId, Long sabadoId) {
		TrimestreResponse trimestre = manageTrimestreUseCase.getById(trimestreId);
		TurmaResponse turma = manageTurmaUseCase.getById(trimestre.turmaId());

		Sabado sabado = sabadoRepository.findById(sabadoId)
				.orElseThrow(() -> new ResourceNotFoundException("Sábado não encontrado: " + sabadoId));
		if (!sabado.getTrimestreId().equals(trimestreId)) {
			throw new BusinessRuleException("Sábado não pertence ao trimestre informado");
		}

		List<Aluno> alunosAtivos = alunoRepository.findActiveByTurmaId(trimestre.turmaId());
		List<Criterio> criterios = criterioRepository.findByTrimestreId(trimestreId);
		List<Nota> notasDoSabado = notaRepository.findByTrimestreIdFiltered(trimestreId, sabadoId, null);

		List<RankingCalculator.RankingEntry> ranking =
				rankingCalculator.calculate(alunosAtivos, notasDoSabado, criterios);

		List<LancamentoPdfRow> rows = ranking.stream()
				.map(item -> new LancamentoPdfRow(
						item.posicao(),
						item.nome(),
						item.total(),
						formatCriterios(item.porCriterio())))
				.toList();

		String sabadoTitulo = sabado.getTitulo() != null && !sabado.getTitulo().isBlank()
				? sabado.getTitulo()
				: "Sábado " + sabado.getNumero();

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("TITULO", "Lançamento — " + sabadoTitulo);
		parameters.put("TRIMESTRE", trimestre.nome());
		parameters.put("TURMA", turma.nome());
		parameters.put("SABADO", sabadoTitulo);

		return lancamentoPdfReportPort.generate(parameters, rows);
	}

	private String formatCriterios(List<RankingCalculator.CriterioTotal> criterios) {
		if (criterios == null || criterios.isEmpty()) {
			return "—";
		}
		return criterios.stream()
				.map(c -> c.nome() + ": " + c.total())
				.collect(Collectors.joining("\n"));
	}
}
