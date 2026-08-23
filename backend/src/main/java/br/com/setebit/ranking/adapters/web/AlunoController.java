package br.com.setebit.ranking.adapters.web;

import br.com.setebit.ranking.adapters.web.dto.AlunoRequest;
import br.com.setebit.ranking.adapters.web.dto.AtivoRequest;
import br.com.setebit.ranking.application.dto.AlunoResponse;
import br.com.setebit.ranking.application.dto.HistoricoAlunoResponse;
import br.com.setebit.ranking.application.port.in.ManageAlunoUseCase;
import br.com.setebit.ranking.application.port.in.ManageNotaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Alunos")
public class AlunoController {

	private final ManageAlunoUseCase manageAlunoUseCase;
	private final ManageNotaUseCase manageNotaUseCase;

	public AlunoController(ManageAlunoUseCase manageAlunoUseCase, ManageNotaUseCase manageNotaUseCase) {
		this.manageAlunoUseCase = manageAlunoUseCase;
		this.manageNotaUseCase = manageNotaUseCase;
	}

	@GetMapping("/api/alunos")
	@Operation(summary = "Listar todos os alunos cadastrados")
	public List<AlunoResponse> listAll() {
		return manageAlunoUseCase.listAll();
	}

	@GetMapping("/api/turmas/{turmaId}/alunos")
	@Operation(summary = "Listar alunos da turma")
	public List<AlunoResponse> listByTurma(@PathVariable Long turmaId) {
		return manageAlunoUseCase.listByTurma(turmaId);
	}

	@PostMapping("/api/turmas/{turmaId}/alunos")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar aluno na turma")
	public AlunoResponse create(@PathVariable Long turmaId, @Valid @RequestBody AlunoRequest request) {
		return manageAlunoUseCase.create(turmaId, request.nome(), request.dataNascimento());
	}

	@GetMapping("/api/alunos/{id}")
	@Operation(summary = "Buscar aluno por id")
	public AlunoResponse get(@PathVariable Long id) {
		return manageAlunoUseCase.getById(id);
	}

	@PutMapping("/api/alunos/{id}")
	@Operation(summary = "Atualizar aluno")
	public AlunoResponse update(@PathVariable Long id, @Valid @RequestBody AlunoRequest request) {
		return manageAlunoUseCase.update(id, request.nome(), request.dataNascimento());
	}

	@PatchMapping("/api/alunos/{id}/ativo")
	@Operation(summary = "Ativar ou inativar aluno")
	public AlunoResponse setAtivo(@PathVariable Long id, @RequestBody AtivoRequest request) {
		return manageAlunoUseCase.setAtivo(id, request.ativo());
	}

	@GetMapping("/api/alunos/{alunoId}/historico")
	@Operation(summary = "Histórico de notas do aluno no período")
	public HistoricoAlunoResponse historico(@PathVariable Long alunoId, @RequestParam Long periodoId) {
		return manageNotaUseCase.historico(alunoId, periodoId);
	}
}
