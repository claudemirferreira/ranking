package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.LancamentoPdfRow;
import br.com.setebit.ranking.application.dto.PeriodoResponse;
import br.com.setebit.ranking.application.dto.TurmaResponse;
import br.com.setebit.ranking.application.port.in.ExportLancamentoPdfUseCase;
import br.com.setebit.ranking.application.port.in.ManagePeriodoUseCase;
import br.com.setebit.ranking.application.port.in.ManageTurmaUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.application.port.out.EncontroRepositoryPort;
import br.com.setebit.ranking.application.port.out.LancamentoPdfReportPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.domain.exception.BusinessRuleException;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Encontro;
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

	private final ManagePeriodoUseCase managePeriodoUseCase;
	private final ManageTurmaUseCase manageTurmaUseCase;
	private final EncontroRepositoryPort encontroRepository;
	private final AlunoRepositoryPort alunoRepository;
	private final NotaRepositoryPort notaRepository;
	private final CriterioRepositoryPort criterioRepository;
	private final LancamentoPdfReportPort lancamentoPdfReportPort;
	private final RankingCalculator rankingCalculator = new RankingCalculator();

	public ExportLancamentoPdfUseCaseImpl(
			ManagePeriodoUseCase managePeriodoUseCase,
			ManageTurmaUseCase manageTurmaUseCase,
			EncontroRepositoryPort encontroRepository,
			AlunoRepositoryPort alunoRepository,
			NotaRepositoryPort notaRepository,
			CriterioRepositoryPort criterioRepository,
			LancamentoPdfReportPort lancamentoPdfReportPort) {
		this.managePeriodoUseCase = managePeriodoUseCase;
		this.manageTurmaUseCase = manageTurmaUseCase;
		this.encontroRepository = encontroRepository;
		this.alunoRepository = alunoRepository;
		this.notaRepository = notaRepository;
		this.criterioRepository = criterioRepository;
		this.lancamentoPdfReportPort = lancamentoPdfReportPort;
	}

	@Override
	public byte[] execute(Long periodoId, Long encontroId) {
		PeriodoResponse periodo = managePeriodoUseCase.getById(periodoId);
		TurmaResponse turma = manageTurmaUseCase.getById(periodo.turmaId());

		Encontro encontro = encontroRepository.findById(encontroId)
				.orElseThrow(() -> new ResourceNotFoundException("Encontro não encontrado: " + encontroId));
		if (!encontro.getPeriodoId().equals(periodoId)) {
			throw new BusinessRuleException("Encontro não pertence ao período informado");
		}

		List<Aluno> alunosAtivos = alunoRepository.findActiveByTurmaId(periodo.turmaId());
		List<Criterio> criterios = criterioRepository.findByPeriodoId(periodoId);
		List<Nota> notasDoEncontro = notaRepository.findByPeriodoIdFiltered(periodoId, encontroId, null);

		List<RankingCalculator.RankingEntry> ranking =
				rankingCalculator.calculate(alunosAtivos, notasDoEncontro, criterios);

		List<LancamentoPdfRow> rows = ranking.stream()
				.map(item -> new LancamentoPdfRow(
						item.posicao(),
						item.nome(),
						item.total(),
						formatCriterios(item.porCriterio())))
				.toList();

		String encontroTitulo = encontro.getTitulo() != null && !encontro.getTitulo().isBlank()
				? encontro.getTitulo()
				: "Sábado " + encontro.getNumero();

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("TITULO", "Lançamento — " + encontroTitulo);
		parameters.put("PERIODO", periodo.nome());
		parameters.put("TURMA", turma.nome());
		parameters.put("ENCONTRO", encontroTitulo);

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
