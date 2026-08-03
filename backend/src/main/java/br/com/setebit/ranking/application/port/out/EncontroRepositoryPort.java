package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.domain.model.Encontro;
import java.util.List;
import java.util.Optional;

public interface EncontroRepositoryPort {

	Encontro save(Encontro encontro);

	List<Encontro> saveAll(List<Encontro> encontros);

	Optional<Encontro> findById(Long id);

	List<Encontro> findByPeriodoId(Long periodoId);

	int countByPeriodoId(Long periodoId);
}
