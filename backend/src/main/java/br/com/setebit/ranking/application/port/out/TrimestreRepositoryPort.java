package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.domain.model.Trimestre;
import java.util.List;
import java.util.Optional;

public interface TrimestreRepositoryPort {

	Trimestre save(Trimestre trimestre);

	Optional<Trimestre> findById(Long id);

	List<Trimestre> findByTurmaId(Long turmaId);
}
