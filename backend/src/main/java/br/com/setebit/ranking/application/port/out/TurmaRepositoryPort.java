package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.domain.model.Turma;
import java.util.List;
import java.util.Optional;

public interface TurmaRepositoryPort {

	Turma save(Turma turma);

	Optional<Turma> findById(Long id);

	List<Turma> findAllActive();
}
