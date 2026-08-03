package br.com.setebit.ranking.infrastructure.persistence.adapter;

import br.com.setebit.ranking.application.port.out.PeriodoRepositoryPort;
import br.com.setebit.ranking.domain.model.Periodo;
import br.com.setebit.ranking.infrastructure.persistence.mapper.JpaMapper;
import br.com.setebit.ranking.infrastructure.persistence.repository.SpringDataPeriodoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PeriodoRepositoryAdapter implements PeriodoRepositoryPort {

	private final SpringDataPeriodoRepository repository;

	public PeriodoRepositoryAdapter(SpringDataPeriodoRepository repository) {
		this.repository = repository;
	}

	@Override
	public Periodo save(Periodo periodo) {
		return JpaMapper.toDomain(repository.save(JpaMapper.toEntity(periodo)));
	}

	@Override
	public Optional<Periodo> findById(Long id) {
		return repository.findById(id).map(JpaMapper::toDomain);
	}

	@Override
	public List<Periodo> findByTurmaId(Long turmaId) {
		return repository.findByTurmaIdOrderByCreatedAtDesc(turmaId).stream().map(JpaMapper::toDomain).toList();
	}
}
