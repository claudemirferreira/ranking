package br.com.setebit.ranking.application.port.in;

import br.com.setebit.ranking.application.dto.TurmaResponse;
import java.util.List;

public interface ManageTurmaUseCase {

	TurmaResponse create(String nome, String descricao);

	TurmaResponse update(Long id, String nome, String descricao);

	TurmaResponse getById(Long id);

	List<TurmaResponse> listAll();

	TurmaResponse softDelete(Long id);
}
