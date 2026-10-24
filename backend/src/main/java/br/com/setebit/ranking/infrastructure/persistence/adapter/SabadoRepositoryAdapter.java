package br.com.setebit.ranking.infrastructure.persistence.adapter;

import br.com.setebit.ranking.application.port.out.SabadoRepositoryPort;
import br.com.setebit.ranking.domain.model.Sabado;
import br.com.setebit.ranking.infrastructure.persistence.mapper.JpaMapper;
import br.com.setebit.ranking.infrastructure.persistence.repository.SpringDataSabadoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class SabadoRepositoryAdapter implements SabadoRepositoryPort {

	private final SpringDataSabadoRepository repository;

	public SabadoRepositoryAdapter(SpringDataSabadoRepository repository) {
		this.repository = repository;
	}

	@Override
	public Sabado save(Sabado sabado) {
		return JpaMapper.toDomain(repository.save(JpaMapper.toEntity(sabado)));
	}

	@Override
	public List<Sabado> saveAll(List<Sabado> sabados) {
		return repository.saveAll(sabados.stream().map(JpaMapper::toEntity).toList()).stream()
				.map(JpaMapper::toDomain)
				.toList();
	}

	@Override
	public Optional<Sabado> findById(Long id) {
		return repository.findById(id).map(JpaMapper::toDomain);
	}

	@Override
	public List<Sabado> findByTrimestreId(Long trimestreId) {
		return repository.findByTrimestreIdOrderByNumeroAsc(trimestreId).stream().map(JpaMapper::toDomain).toList();
	}

	@Override
	public int countByTrimestreId(Long trimestreId) {
		return repository.countByTrimestreId(trimestreId);
	}
}
