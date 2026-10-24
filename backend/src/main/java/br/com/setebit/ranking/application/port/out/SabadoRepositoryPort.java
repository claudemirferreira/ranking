package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.domain.model.Sabado;
import java.util.List;
import java.util.Optional;

public interface SabadoRepositoryPort {

	Sabado save(Sabado sabado);

	List<Sabado> saveAll(List<Sabado> sabados);

	Optional<Sabado> findById(Long id);

	List<Sabado> findByTrimestreId(Long trimestreId);

	int countByTrimestreId(Long trimestreId);
}
