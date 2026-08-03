package br.com.setebit.ranking.adapters.web;

import br.com.setebit.ranking.adapters.web.dto.TurmaRequest;
import br.com.setebit.ranking.application.dto.TurmaResponse;
import br.com.setebit.ranking.application.port.in.ManageTurmaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/turmas")
@Tag(name = "Turmas")
public class TurmaController {

	private final ManageTurmaUseCase manageTurmaUseCase;

	public TurmaController(ManageTurmaUseCase manageTurmaUseCase) {
		this.manageTurmaUseCase = manageTurmaUseCase;
	}

	@GetMapping
	@Operation(summary = "Listar turmas ativas")
	public List<TurmaResponse> list() {
		return manageTurmaUseCase.listAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar turma")
	public TurmaResponse create(@Valid @RequestBody TurmaRequest request) {
		return manageTurmaUseCase.create(request.nome(), request.descricao());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar turma por id")
	public TurmaResponse get(@PathVariable Long id) {
		return manageTurmaUseCase.getById(id);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar turma")
	public TurmaResponse update(@PathVariable Long id, @Valid @RequestBody TurmaRequest request) {
		return manageTurmaUseCase.update(id, request.nome(), request.descricao());
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir turma (soft delete)")
	public TurmaResponse delete(@PathVariable Long id) {
		return manageTurmaUseCase.softDelete(id);
	}
}
