package br.com.setebit.ranking.infrastructure.persistence.adapter;

import br.com.setebit.ranking.application.port.out.EncontroRepositoryPort;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.infrastructure.persistence.mapper.JpaMapper;
import br.com.setebit.ranking.infrastructure.persistence.repository.SpringDataEncontroRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class EncontroRepositoryAdapter implements EncontroRepositoryPort {

	private final SpringDataEncontroRepository repository;

	public EncontroRepositoryAdapter(SpringDataEncontroRepository repository) {
		this.repository = repository;
	}

	@Override
	public Encontro save(Encontro encontro) {
		return JpaMapper.toDomain(repository.save(JpaMapper.toEntity(encontro)));
	}

	@Override
	public List<Encontro> saveAll(List<Encontro> encontros) {
		return repository.saveAll(encontros.stream().map(JpaMapper::toEntity).toList()).stream()
				.map(JpaMapper::toDomain)
				.toList();
	}

	@Override
	public Optional<Encontro> findById(Long id) {
		return repository.findById(id).map(JpaMapper::toDomain);
	}

	@Override
	public List<Encontro> findByPeriodoId(Long periodoId) {
		return repository.findByPeriodoIdOrderByNumeroAsc(periodoId).stream().map(JpaMapper::toDomain).toList();
	}

	@Override
	public int countByPeriodoId(Long periodoId) {
		return repository.countByPeriodoId(periodoId);
	}
}
