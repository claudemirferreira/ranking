package br.com.setebit.ranking.infrastructure.persistence.adapter;

import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.infrastructure.persistence.mapper.JpaMapper;
import br.com.setebit.ranking.infrastructure.persistence.repository.SpringDataNotaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class NotaRepositoryAdapter implements NotaRepositoryPort {

	private final SpringDataNotaRepository repository;

	public NotaRepositoryAdapter(SpringDataNotaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Nota save(Nota nota) {
		return JpaMapper.toDomain(repository.save(JpaMapper.toEntity(nota)));
	}

	@Override
	public List<Nota> saveAll(List<Nota> notas) {
		return repository.saveAll(notas.stream().map(JpaMapper::toEntity).toList()).stream()
				.map(JpaMapper::toDomain)
				.toList();
	}

	@Override
	public Optional<Nota> findByAlunoIdAndSabadoIdAndCriterioId(Long alunoId, Long sabadoId, Long criterioId) {
		return repository.findByAlunoIdAndSabadoIdAndCriterioId(alunoId, sabadoId, criterioId).map(JpaMapper::toDomain);
	}

	@Override
	public List<Nota> findByTrimestreId(Long trimestreId) {
		return repository.findByTrimestreId(trimestreId).stream().map(JpaMapper::toDomain).toList();
	}

	@Override
	public List<Nota> findByTrimestreIdFiltered(Long trimestreId, Long sabadoId, Long criterioId) {
		return repository.findByTrimestreIdFiltered(trimestreId, sabadoId, criterioId).stream()
				.map(JpaMapper::toDomain)
				.toList();
	}

	@Override
	public List<Nota> findByAlunoIdAndTrimestreId(Long alunoId, Long trimestreId) {
		return repository.findByAlunoIdAndTrimestreId(alunoId, trimestreId).stream().map(JpaMapper::toDomain).toList();
	}

	@Override
	public void deleteByAlunoId(Long alunoId) {
		repository.deleteByAlunoId(alunoId);
	}
}
