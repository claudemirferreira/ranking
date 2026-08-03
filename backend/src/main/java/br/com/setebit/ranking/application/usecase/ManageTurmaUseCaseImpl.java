package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.TurmaResponse;
import br.com.setebit.ranking.application.mapper.ApplicationMapper;
import br.com.setebit.ranking.application.port.in.ManageTurmaUseCase;
import br.com.setebit.ranking.application.port.out.TurmaRepositoryPort;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Turma;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManageTurmaUseCaseImpl implements ManageTurmaUseCase {

	private final TurmaRepositoryPort turmaRepository;

	public ManageTurmaUseCaseImpl(TurmaRepositoryPort turmaRepository) {
		this.turmaRepository = turmaRepository;
	}

	@Override
	public TurmaResponse create(String nome, String descricao) {
		Instant now = Instant.now();
		Turma turma = new Turma();
		turma.setNome(nome.trim());
		turma.setDescricao(descricao);
		turma.setAtivo(true);
		turma.setCreatedAt(now);
		turma.setUpdatedAt(now);
		return ApplicationMapper.toResponse(turmaRepository.save(turma));
	}

	@Override
	public TurmaResponse update(Long id, String nome, String descricao) {
		Turma turma = requireTurma(id);
		turma.setNome(nome.trim());
		turma.setDescricao(descricao);
		turma.setUpdatedAt(Instant.now());
		return ApplicationMapper.toResponse(turmaRepository.save(turma));
	}

	@Override
	@Transactional(readOnly = true)
	public TurmaResponse getById(Long id) {
		return ApplicationMapper.toResponse(requireTurma(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<TurmaResponse> listAll() {
		return turmaRepository.findAllActive().stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	public TurmaResponse softDelete(Long id) {
		Turma turma = requireTurma(id);
		turma.softDelete();
		return ApplicationMapper.toResponse(turmaRepository.save(turma));
	}

	private Turma requireTurma(Long id) {
		return turmaRepository.findById(id)
				.filter(Turma::isAtivo)
				.orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada: " + id));
	}
}
