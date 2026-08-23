package br.com.setebit.ranking.infrastructure.persistence.adapter;

import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.infrastructure.persistence.mapper.JpaMapper;
import br.com.setebit.ranking.infrastructure.persistence.repository.SpringDataAlunoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AlunoRepositoryAdapter implements AlunoRepositoryPort {

	private final SpringDataAlunoRepository repository;

	public AlunoRepositoryAdapter(SpringDataAlunoRepository repository) {
		this.repository = repository;
	}

	@Override
	public Aluno save(Aluno aluno) {
		return JpaMapper.toDomain(repository.save(JpaMapper.toEntity(aluno)));
	}

	@Override
	public Optional<Aluno> findById(Long id) {
		return repository.findById(id).map(JpaMapper::toDomain);
	}

	@Override
	public List<Aluno> findAll() {
		return repository.findAllByOrderByTurmaIdAscNomeAsc().stream().map(JpaMapper::toDomain).toList();
	}

	@Override
	public List<Aluno> findByTurmaId(Long turmaId) {
		return repository.findByTurmaIdOrderByNomeAsc(turmaId).stream().map(JpaMapper::toDomain).toList();
	}

	@Override
	public List<Aluno> findActiveByTurmaId(Long turmaId) {
		return repository.findByTurmaIdAndAtivoTrueOrderByNomeAsc(turmaId).stream()
				.map(JpaMapper::toDomain)
				.toList();
	}
}
