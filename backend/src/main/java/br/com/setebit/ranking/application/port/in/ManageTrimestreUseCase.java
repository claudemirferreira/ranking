package br.com.setebit.ranking.application.port.in;

import br.com.setebit.ranking.application.dto.CriterioResponse;
import br.com.setebit.ranking.application.dto.EncontroResponse;
import br.com.setebit.ranking.application.dto.TrimestreResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ManageTrimestreUseCase {

	TrimestreResponse create(Long turmaId, String nome, LocalDate dataInicio, LocalDate dataFim);

	TrimestreResponse update(Long id, String nome, LocalDate dataInicio, LocalDate dataFim, Boolean ativo);

	TrimestreResponse getById(Long id);

	List<TrimestreResponse> listByTurma(Long turmaId);

	List<EncontroResponse> createEncontros(Long trimestreId, int quantidade);

	List<EncontroResponse> listEncontros(Long trimestreId);

	CriterioResponse createCriterio(Long trimestreId, String nome, Integer ordem, BigDecimal pontuacaoMaxima);

	CriterioResponse updateCriterio(Long id, String nome, Integer ordem, BigDecimal pontuacaoMaxima);

	List<CriterioResponse> listCriterios(Long trimestreId);
}
