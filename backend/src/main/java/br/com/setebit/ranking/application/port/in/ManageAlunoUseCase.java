package br.com.setebit.ranking.application.port.in;

import br.com.setebit.ranking.application.dto.AlunoResponse;
import java.util.List;

public interface ManageAlunoUseCase {

	AlunoResponse create(Long turmaId, Integer numero, String nome);

	AlunoResponse update(Long id, Integer numero, String nome);

	AlunoResponse getById(Long id);

	List<AlunoResponse> listAll();

	List<AlunoResponse> listByTurma(Long turmaId);

	AlunoResponse setAtivo(Long id, boolean ativo);
}
