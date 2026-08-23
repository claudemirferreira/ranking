package br.com.setebit.ranking.application.port.in;

import br.com.setebit.ranking.application.dto.AlunoResponse;
import java.time.LocalDate;
import java.util.List;

public interface ManageAlunoUseCase {

	AlunoResponse create(Long turmaId, String nome, LocalDate dataNascimento);

	AlunoResponse update(Long id, String nome, LocalDate dataNascimento);

	AlunoResponse getById(Long id);

	List<AlunoResponse> listAll();

	List<AlunoResponse> listByTurma(Long turmaId);

	AlunoResponse setAtivo(Long id, boolean ativo);
}
