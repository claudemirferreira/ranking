package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.domain.model.Nota;
import java.util.List;
import java.util.Optional;

public interface NotaRepositoryPort {

	Nota save(Nota nota);

	List<Nota> saveAll(List<Nota> notas);

	Optional<Nota> findByAlunoIdAndEncontroIdAndCriterioId(Long alunoId, Long encontroId, Long criterioId);

	List<Nota> findByPeriodoId(Long periodoId);

	List<Nota> findByPeriodoIdFiltered(Long periodoId, Long encontroId, Long criterioId);

	List<Nota> findByAlunoIdAndPeriodoId(Long alunoId, Long periodoId);

	void deleteByAlunoId(Long alunoId);
}
