package br.com.setebit.ranking.adapters.web;

import br.com.setebit.ranking.adapters.web.dto.CreateEncontrosRequest;
import br.com.setebit.ranking.adapters.web.dto.CriterioRequest;
import br.com.setebit.ranking.adapters.web.dto.PeriodoRequest;
import br.com.setebit.ranking.application.dto.CriterioResponse;
import br.com.setebit.ranking.application.dto.EncontroResponse;
import br.com.setebit.ranking.application.dto.EvolucaoPontuacaoResponse;
import br.com.setebit.ranking.application.dto.NotaResponse;
import br.com.setebit.ranking.application.dto.PeriodoResponse;
import br.com.setebit.ranking.application.dto.PerdaPontuacaoResponse;
import br.com.setebit.ranking.application.dto.RankingItemResponse;
import br.com.setebit.ranking.application.port.in.ExportLancamentoPdfUseCase;
import br.com.setebit.ranking.application.port.in.ExportRankingPdfUseCase;
import br.com.setebit.ranking.application.port.in.GetEvolucaoPontuacaoUseCase;
import br.com.setebit.ranking.application.port.in.GetPerdaPontuacaoUseCase;
import br.com.setebit.ranking.application.port.in.GetRankingUseCase;
import br.com.setebit.ranking.application.port.in.ManageNotaUseCase;
import br.com.setebit.ranking.application.port.in.ManagePeriodoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Períodos")
public class PeriodoController {

	private final ManagePeriodoUseCase managePeriodoUseCase;
	private final ManageNotaUseCase manageNotaUseCase;
	private final GetRankingUseCase getRankingUseCase;
	private final GetEvolucaoPontuacaoUseCase getEvolucaoPontuacaoUseCase;
	private final GetPerdaPontuacaoUseCase getPerdaPontuacaoUseCase;
	private final ExportRankingPdfUseCase exportRankingPdfUseCase;
	private final ExportLancamentoPdfUseCase exportLancamentoPdfUseCase;

	public PeriodoController(
			ManagePeriodoUseCase managePeriodoUseCase,
			ManageNotaUseCase manageNotaUseCase,
			GetRankingUseCase getRankingUseCase,
			GetEvolucaoPontuacaoUseCase getEvolucaoPontuacaoUseCase,
			GetPerdaPontuacaoUseCase getPerdaPontuacaoUseCase,
			ExportRankingPdfUseCase exportRankingPdfUseCase,
			ExportLancamentoPdfUseCase exportLancamentoPdfUseCase) {
		this.managePeriodoUseCase = managePeriodoUseCase;
		this.manageNotaUseCase = manageNotaUseCase;
		this.getRankingUseCase = getRankingUseCase;
		this.getEvolucaoPontuacaoUseCase = getEvolucaoPontuacaoUseCase;
		this.getPerdaPontuacaoUseCase = getPerdaPontuacaoUseCase;
		this.exportRankingPdfUseCase = exportRankingPdfUseCase;
		this.exportLancamentoPdfUseCase = exportLancamentoPdfUseCase;
	}

	@GetMapping("/api/turmas/{turmaId}/periodos")
	@Operation(summary = "Listar períodos da turma")
	public List<PeriodoResponse> listByTurma(@PathVariable Long turmaId) {
		return managePeriodoUseCase.listByTurma(turmaId);
	}

	@PostMapping("/api/turmas/{turmaId}/periodos")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar período")
	public PeriodoResponse create(@PathVariable Long turmaId, @Valid @RequestBody PeriodoRequest request) {
		return managePeriodoUseCase.create(turmaId, request.nome(), request.dataInicio(), request.dataFim());
	}

	@GetMapping("/api/periodos/{id}")
	@Operation(summary = "Buscar período")
	public PeriodoResponse get(@PathVariable Long id) {
		return managePeriodoUseCase.getById(id);
	}

	@PutMapping("/api/periodos/{id}")
	@Operation(summary = "Atualizar período")
	public PeriodoResponse update(@PathVariable Long id, @Valid @RequestBody PeriodoRequest request) {
		return managePeriodoUseCase.update(id, request.nome(), request.dataInicio(), request.dataFim(), request.ativo());
	}

	@PostMapping("/api/periodos/{id}/encontros")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar encontros (sábados) em lote")
	public List<EncontroResponse> createEncontros(
			@PathVariable Long id, @Valid @RequestBody CreateEncontrosRequest request) {
		return managePeriodoUseCase.createEncontros(id, request.quantidade());
	}

	@GetMapping("/api/periodos/{id}/encontros")
	@Operation(summary = "Listar encontros do período")
	public List<EncontroResponse> listEncontros(@PathVariable Long id) {
		return managePeriodoUseCase.listEncontros(id);
	}

	@PostMapping("/api/periodos/{id}/criterios")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar critério de pontuação")
	public CriterioResponse createCriterio(@PathVariable Long id, @Valid @RequestBody CriterioRequest request) {
		return managePeriodoUseCase.createCriterio(id, request.nome(), request.ordem(), request.pontuacaoMaxima());
	}

	@GetMapping("/api/periodos/{id}/criterios")
	@Operation(summary = "Listar critérios do período")
	public List<CriterioResponse> listCriterios(@PathVariable Long id) {
		return managePeriodoUseCase.listCriterios(id);
	}

	@PutMapping("/api/criterios/{id}")
	@Operation(summary = "Atualizar critério")
	public CriterioResponse updateCriterio(@PathVariable Long id, @Valid @RequestBody CriterioRequest request) {
		return managePeriodoUseCase.updateCriterio(id, request.nome(), request.ordem(), request.pontuacaoMaxima());
	}

	@GetMapping("/api/periodos/{periodoId}/notas")
	@Operation(summary = "Listar notas do período (filtros opcionais)")
	public List<NotaResponse> listNotas(
			@PathVariable Long periodoId,
			@RequestParam(required = false) Long encontroId,
			@RequestParam(required = false) Long criterioId) {
		return manageNotaUseCase.listByPeriodo(periodoId, encontroId, criterioId);
	}

	@GetMapping("/api/periodos/{periodoId}/ranking")
	@Operation(summary = "Ranking do período (apenas alunos ativos)")
	public List<RankingItemResponse> ranking(@PathVariable Long periodoId) {
		return getRankingUseCase.execute(periodoId);
	}

	@GetMapping("/api/periodos/{periodoId}/dashboard/evolucao")
	@Operation(summary = "Evolução da pontuação de cada aluno por sábado")
	public EvolucaoPontuacaoResponse evolucao(@PathVariable Long periodoId) {
		return getEvolucaoPontuacaoUseCase.execute(periodoId);
	}

	@GetMapping("/api/periodos/{periodoId}/dashboard/perdas")
	@Operation(summary = "Critérios em que os alunos mais perdem pontos")
	public PerdaPontuacaoResponse perdas(@PathVariable Long periodoId) {
		return getPerdaPontuacaoUseCase.execute(periodoId);
	}

	@GetMapping("/api/periodos/{periodoId}/ranking/pdf")
	@Operation(summary = "Exportar ranking em PDF (nome, total e critério)")
	public ResponseEntity<byte[]> rankingPdf(@PathVariable Long periodoId) {
		byte[] pdf = exportRankingPdfUseCase.execute(periodoId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ranking-periodo-" + periodoId + ".pdf\"")
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdf);
	}

	@GetMapping("/api/periodos/{periodoId}/encontros/{encontroId}/lancamento/pdf")
	@Operation(summary = "Imprimir lançamento do encontro em PDF (ordenado por total decrescente)")
	public ResponseEntity<byte[]> lancamentoPdf(
			@PathVariable Long periodoId,
			@PathVariable Long encontroId) {
		byte[] pdf = exportLancamentoPdfUseCase.execute(periodoId, encontroId);
		return ResponseEntity.ok()
				.header(
						HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"lancamento-periodo-" + periodoId + "-encontro-" + encontroId + ".pdf\"")
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdf);
	}
}
