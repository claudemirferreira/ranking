package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.domain.model.Criterio;
import java.util.List;
import java.util.Optional;

public interface CriterioRepositoryPort {

	Criterio save(Criterio criterio);

	Optional<Criterio> findById(Long id);

	List<Criterio> findByPeriodoId(Long periodoId);
}
