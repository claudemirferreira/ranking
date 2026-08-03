package br.com.setebit.ranking.infrastructure.persistence.adapter;

import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.infrastructure.persistence.mapper.JpaMapper;
import br.com.setebit.ranking.infrastructure.persistence.repository.SpringDataCriterioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CriterioRepositoryAdapter implements CriterioRepositoryPort {

	private final SpringDataCriterioRepository repository;

	public CriterioRepositoryAdapter(SpringDataCriterioRepository repository) {
		this.repository = repository;
	}

	@Override
	public Criterio save(Criterio criterio) {
		return JpaMapper.toDomain(repository.save(JpaMapper.toEntity(criterio)));
	}

	@Override
	public Optional<Criterio> findById(Long id) {
		return repository.findById(id).map(JpaMapper::toDomain);
	}

	@Override
	public List<Criterio> findByPeriodoId(Long periodoId) {
		return repository.findByPeriodoIdOrderByOrdemAsc(periodoId).stream().map(JpaMapper::toDomain).toList();
	}
}
