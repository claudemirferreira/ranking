package br.com.setebit.ranking.infrastructure.persistence.adapter;

import br.com.setebit.ranking.application.port.out.TrimestreRepositoryPort;
import br.com.setebit.ranking.domain.model.Trimestre;
import br.com.setebit.ranking.infrastructure.persistence.mapper.JpaMapper;
import br.com.setebit.ranking.infrastructure.persistence.repository.SpringDataTrimestreRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TrimestreRepositoryAdapter implements TrimestreRepositoryPort {

	private final SpringDataTrimestreRepository repository;

	public TrimestreRepositoryAdapter(SpringDataTrimestreRepository repository) {
		this.repository = repository;
	}

	@Override
	public Trimestre save(Trimestre trimestre) {
		return JpaMapper.toDomain(repository.save(JpaMapper.toEntity(trimestre)));
	}

	@Override
	public Optional<Trimestre> findById(Long id) {
		return repository.findById(id).map(JpaMapper::toDomain);
	}

	@Override
	public List<Trimestre> findByTurmaId(Long turmaId) {
		return repository.findByTurmaIdOrderByCreatedAtDesc(turmaId).stream().map(JpaMapper::toDomain).toList();
	}
}
