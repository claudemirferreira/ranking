package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.domain.model.Periodo;
import java.util.List;
import java.util.Optional;

public interface PeriodoRepositoryPort {

	Periodo save(Periodo periodo);

	Optional<Periodo> findById(Long id);

	List<Periodo> findByTurmaId(Long turmaId);
}
