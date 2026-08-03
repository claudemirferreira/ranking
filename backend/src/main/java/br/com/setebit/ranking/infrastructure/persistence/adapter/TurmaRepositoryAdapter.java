package br.com.setebit.ranking.infrastructure.persistence.adapter;

import br.com.setebit.ranking.application.port.out.TurmaRepositoryPort;
import br.com.setebit.ranking.domain.model.Turma;
import br.com.setebit.ranking.infrastructure.persistence.mapper.JpaMapper;
import br.com.setebit.ranking.infrastructure.persistence.repository.SpringDataTurmaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TurmaRepositoryAdapter implements TurmaRepositoryPort {

	private final SpringDataTurmaRepository repository;

	public TurmaRepositoryAdapter(SpringDataTurmaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Turma save(Turma turma) {
		return JpaMapper.toDomain(repository.save(JpaMapper.toEntity(turma)));
	}

	@Override
	public Optional<Turma> findById(Long id) {
		return repository.findById(id).map(JpaMapper::toDomain);
	}

	@Override
	public List<Turma> findAllActive() {
		return repository.findByAtivoTrueOrderByNomeAsc().stream().map(JpaMapper::toDomain).toList();
	}
}
