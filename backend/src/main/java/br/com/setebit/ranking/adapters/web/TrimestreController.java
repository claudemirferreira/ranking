package br.com.setebit.ranking.adapters.web;

import br.com.setebit.ranking.adapters.web.dto.CreateEncontrosRequest;
import br.com.setebit.ranking.adapters.web.dto.CriterioRequest;
import br.com.setebit.ranking.adapters.web.dto.TrimestreRequest;
import br.com.setebit.ranking.application.dto.CriterioResponse;
import br.com.setebit.ranking.application.dto.EncontroResponse;
import br.com.setebit.ranking.application.dto.EvolucaoPontuacaoResponse;
import br.com.setebit.ranking.application.dto.NotaResponse;
import br.com.setebit.ranking.application.dto.PerdaPontuacaoResponse;
import br.com.setebit.ranking.application.dto.TrimestreResponse;
import br.com.setebit.ranking.application.dto.RankingItemResponse;
import br.com.setebit.ranking.application.port.in.ExportLancamentoPdfUseCase;
import br.com.setebit.ranking.application.port.in.ExportRankingPdfUseCase;
import br.com.setebit.ranking.application.port.in.GetEvolucaoPontuacaoUseCase;
import br.com.setebit.ranking.application.port.in.GetPerdaPontuacaoUseCase;
import br.com.setebit.ranking.application.port.in.GetRankingUseCase;
import br.com.setebit.ranking.application.port.in.ManageNotaUseCase;
import br.com.setebit.ranking.application.port.in.ManageTrimestreUseCase;
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
@Tag(name = "Trimestres")
public class TrimestreController {

	private final ManageTrimestreUseCase manageTrimestreUseCase;
	private final ManageNotaUseCase manageNotaUseCase;
	private final GetRankingUseCase getRankingUseCase;
	private final GetEvolucaoPontuacaoUseCase getEvolucaoPontuacaoUseCase;
	private final GetPerdaPontuacaoUseCase getPerdaPontuacaoUseCase;
	private final ExportRankingPdfUseCase exportRankingPdfUseCase;
	private final ExportLancamentoPdfUseCase exportLancamentoPdfUseCase;

	public TrimestreController(
			ManageTrimestreUseCase manageTrimestreUseCase,
			ManageNotaUseCase manageNotaUseCase,
			GetRankingUseCase getRankingUseCase,
			GetEvolucaoPontuacaoUseCase getEvolucaoPontuacaoUseCase,
			GetPerdaPontuacaoUseCase getPerdaPontuacaoUseCase,
			ExportRankingPdfUseCase exportRankingPdfUseCase,
			ExportLancamentoPdfUseCase exportLancamentoPdfUseCase) {
		this.manageTrimestreUseCase = manageTrimestreUseCase;
		this.manageNotaUseCase = manageNotaUseCase;
		this.getRankingUseCase = getRankingUseCase;
		this.getEvolucaoPontuacaoUseCase = getEvolucaoPontuacaoUseCase;
		this.getPerdaPontuacaoUseCase = getPerdaPontuacaoUseCase;
		this.exportRankingPdfUseCase = exportRankingPdfUseCase;
		this.exportLancamentoPdfUseCase = exportLancamentoPdfUseCase;
	}

	@GetMapping("/api/turmas/{turmaId}/trimestres")
	@Operation(summary = "Listar trimestres da turma")
	public List<TrimestreResponse> listByTurma(@PathVariable Long turmaId) {
		return manageTrimestreUseCase.listByTurma(turmaId);
	}

	@PostMapping("/api/turmas/{turmaId}/trimestres")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar trimestre")
	public TrimestreResponse create(@PathVariable Long turmaId, @Valid @RequestBody TrimestreRequest request) {
		return manageTrimestreUseCase.create(turmaId, request.nome(), request.dataInicio(), request.dataFim());
	}

	@GetMapping("/api/trimestres/{id}")
	@Operation(summary = "Buscar trimestre")
	public TrimestreResponse get(@PathVariable Long id) {
		return manageTrimestreUseCase.getById(id);
	}

	@PutMapping("/api/trimestres/{id}")
	@Operation(summary = "Atualizar trimestre")
	public TrimestreResponse update(@PathVariable Long id, @Valid @RequestBody TrimestreRequest request) {
		return manageTrimestreUseCase.update(id, request.nome(), request.dataInicio(), request.dataFim(), request.ativo());
	}

	@PostMapping("/api/trimestres/{id}/encontros")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar encontros (sábados) em lote")
	public List<EncontroResponse> createEncontros(
			@PathVariable Long id, @Valid @RequestBody CreateEncontrosRequest request) {
		return manageTrimestreUseCase.createEncontros(id, request.quantidade());
	}

	@GetMapping("/api/trimestres/{id}/encontros")
	@Operation(summary = "Listar encontros do trimestre")
	public List<EncontroResponse> listEncontros(@PathVariable Long id) {
		return manageTrimestreUseCase.listEncontros(id);
	}

	@PostMapping("/api/trimestres/{id}/criterios")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar critério de pontuação")
	public CriterioResponse createCriterio(@PathVariable Long id, @Valid @RequestBody CriterioRequest request) {
		return manageTrimestreUseCase.createCriterio(id, request.nome(), request.ordem(), request.pontuacaoMaxima());
	}

	@GetMapping("/api/trimestres/{id}/criterios")
	@Operation(summary = "Listar critérios do trimestre")
	public List<CriterioResponse> listCriterios(@PathVariable Long id) {
		return manageTrimestreUseCase.listCriterios(id);
	}

	@PutMapping("/api/criterios/{id}")
	@Operation(summary = "Atualizar critério")
	public CriterioResponse updateCriterio(@PathVariable Long id, @Valid @RequestBody CriterioRequest request) {
		return manageTrimestreUseCase.updateCriterio(id, request.nome(), request.ordem(), request.pontuacaoMaxima());
	}

	@GetMapping("/api/trimestres/{trimestreId}/notas")
	@Operation(summary = "Listar notas do trimestre (filtros opcionais)")
	public List<NotaResponse> listNotas(
			@PathVariable Long trimestreId,
			@RequestParam(required = false) Long encontroId,
			@RequestParam(required = false) Long criterioId) {
		return manageNotaUseCase.listByTrimestre(trimestreId, encontroId, criterioId);
	}

	@GetMapping("/api/trimestres/{trimestreId}/evolucao")
	@Operation(summary = "Evolução da pontuação dos alunos por sábado")
	public EvolucaoPontuacaoResponse evolucao(@PathVariable Long trimestreId) {
		return getEvolucaoPontuacaoUseCase.execute(trimestreId);
	}

	@GetMapping("/api/trimestres/{trimestreId}/perdas")
	@Operation(summary = "Onde os alunos perdem mais pontos no trimestre")
	public PerdaPontuacaoResponse perdas(@PathVariable Long trimestreId) {
		return getPerdaPontuacaoUseCase.execute(trimestreId);
	}

	@GetMapping("/api/trimestres/{trimestreId}/ranking")
	@Operation(summary = "Ranking do trimestre (apenas alunos ativos)")
	public List<RankingItemResponse> ranking(@PathVariable Long trimestreId) {
		return getRankingUseCase.execute(trimestreId);
	}

	@GetMapping("/api/trimestres/{trimestreId}/ranking/pdf")
	@Operation(summary = "Exportar ranking em PDF (nome, total e critério)")
	public ResponseEntity<byte[]> rankingPdf(@PathVariable Long trimestreId) {
		byte[] pdf = exportRankingPdfUseCase.execute(trimestreId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ranking-trimestre-" + trimestreId + ".pdf\"")
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdf);
	}

	@GetMapping("/api/trimestres/{trimestreId}/encontros/{encontroId}/lancamento/pdf")
	@Operation(summary = "Imprimir lançamento do encontro em PDF (ordenado por total decrescente)")
	public ResponseEntity<byte[]> lancamentoPdf(
			@PathVariable Long trimestreId,
			@PathVariable Long encontroId) {
		byte[] pdf = exportLancamentoPdfUseCase.execute(trimestreId, encontroId);
		return ResponseEntity.ok()
				.header(
						HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"lancamento-trimestre-" + trimestreId + "-encontro-" + encontroId + ".pdf\"")
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdf);
	}
}
