package br.com.setebit.ranking.adapters.web;

import br.com.setebit.ranking.adapters.web.dto.NotaLoteRequest;
import br.com.setebit.ranking.adapters.web.dto.NotaRequest;
import br.com.setebit.ranking.application.dto.NotaResponse;
import br.com.setebit.ranking.application.port.in.ManageNotaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notas")
@Tag(name = "Notas")
public class NotaController {

	private final ManageNotaUseCase manageNotaUseCase;

	public NotaController(ManageNotaUseCase manageNotaUseCase) {
		this.manageNotaUseCase = manageNotaUseCase;
	}

	@PutMapping
	@Operation(summary = "Upsert de uma nota")
	public NotaResponse upsert(@Valid @RequestBody NotaRequest request) {
		return manageNotaUseCase.upsert(
				request.alunoId(),
				request.encontroId(),
				request.criterioId(),
				request.valor(),
				request.observacao());
	}

	@PutMapping("/lote")
	@Operation(summary = "Upsert de notas em lote")
	public List<NotaResponse> upsertLote(@Valid @RequestBody NotaLoteRequest request) {
		List<ManageNotaUseCase.NotaItem> itens = request.itens().stream()
				.map(i -> new ManageNotaUseCase.NotaItem(
						i.alunoId(), i.encontroId(), i.criterioId(), i.valor(), i.observacao()))
				.toList();
		return manageNotaUseCase.upsertLote(itens);
	}
}
