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
	public Optional<Nota> findByAlunoIdAndEncontroIdAndCriterioId(Long alunoId, Long encontroId, Long criterioId) {
		return repository.findByAlunoIdAndEncontroIdAndCriterioId(alunoId, encontroId, criterioId).map(JpaMapper::toDomain);
	}

	@Override
	public List<Nota> findByPeriodoId(Long periodoId) {
		return repository.findByPeriodoId(periodoId).stream().map(JpaMapper::toDomain).toList();
	}

	@Override
	public List<Nota> findByPeriodoIdFiltered(Long periodoId, Long encontroId, Long criterioId) {
		return repository.findByPeriodoIdFiltered(periodoId, encontroId, criterioId).stream()
				.map(JpaMapper::toDomain)
				.toList();
	}

	@Override
	public List<Nota> findByAlunoIdAndPeriodoId(Long alunoId, Long periodoId) {
		return repository.findByAlunoIdAndPeriodoId(alunoId, periodoId).stream().map(JpaMapper::toDomain).toList();
	}
}
