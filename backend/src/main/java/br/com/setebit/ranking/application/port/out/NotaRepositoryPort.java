package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.domain.model.Nota;
import java.util.List;
import java.util.Optional;

public interface NotaRepositoryPort {

	Nota save(Nota nota);

	List<Nota> saveAll(List<Nota> notas);

	Optional<Nota> findByAlunoIdAndSabadoIdAndCriterioId(Long alunoId, Long sabadoId, Long criterioId);

	List<Nota> findByTrimestreId(Long trimestreId);

	List<Nota> findByTrimestreIdFiltered(Long trimestreId, Long sabadoId, Long criterioId);

	List<Nota> findByAlunoIdAndTrimestreId(Long alunoId, Long trimestreId);

	void deleteByAlunoId(Long alunoId);
}
