package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.domain.model.Aluno;
import java.util.List;
import java.util.Optional;

public interface AlunoRepositoryPort {

	Aluno save(Aluno aluno);

	Optional<Aluno> findById(Long id);

	List<Aluno> findAll();

	List<Aluno> findByTurmaId(Long turmaId);

	List<Aluno> findActiveByTurmaId(Long turmaId);
}
