package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.PeriodoResponse;
import br.com.setebit.ranking.application.dto.RankingItemResponse;
import br.com.setebit.ranking.application.dto.RankingPdfRow;
import br.com.setebit.ranking.application.dto.TurmaResponse;
import br.com.setebit.ranking.application.port.in.ExportRankingPdfUseCase;
import br.com.setebit.ranking.application.port.in.GetRankingUseCase;
import br.com.setebit.ranking.application.port.in.ManagePeriodoUseCase;
import br.com.setebit.ranking.application.port.in.ManageTurmaUseCase;
import br.com.setebit.ranking.application.port.out.RankingPdfReportPort;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ExportRankingPdfUseCaseImpl implements ExportRankingPdfUseCase {

	private final GetRankingUseCase getRankingUseCase;
	private final ManagePeriodoUseCase managePeriodoUseCase;
	private final ManageTurmaUseCase manageTurmaUseCase;
	private final RankingPdfReportPort rankingPdfReportPort;

	public ExportRankingPdfUseCaseImpl(
			GetRankingUseCase getRankingUseCase,
			ManagePeriodoUseCase managePeriodoUseCase,
			ManageTurmaUseCase manageTurmaUseCase,
			RankingPdfReportPort rankingPdfReportPort) {
		this.getRankingUseCase = getRankingUseCase;
		this.managePeriodoUseCase = managePeriodoUseCase;
		this.manageTurmaUseCase = manageTurmaUseCase;
		this.rankingPdfReportPort = rankingPdfReportPort;
	}

	@Override
	public byte[] execute(Long periodoId) {
		PeriodoResponse periodo = managePeriodoUseCase.getById(periodoId);
		TurmaResponse turma = manageTurmaUseCase.getById(periodo.turmaId());
		List<RankingItemResponse> ranking = getRankingUseCase.execute(periodoId);

		List<RankingPdfRow> rows = ranking.stream()
				.map(item -> new RankingPdfRow(
						item.nome(),
						item.total(),
						formatCriterios(item.porCriterio())))
				.toList();

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("TITULO", "Ranking — " + periodo.nome());
		parameters.put("PERIODO", periodo.nome());
		parameters.put("TURMA", turma.nome());

		return rankingPdfReportPort.generate(parameters, rows);
	}

	private String formatCriterios(List<RankingItemResponse.CriterioTotalResponse> criterios) {
		if (criterios == null || criterios.isEmpty()) {
			return "—";
		}
		return criterios.stream()
				.map(c -> c.nome() + ": " + c.total())
				.collect(Collectors.joining("\n"));
	}
}
