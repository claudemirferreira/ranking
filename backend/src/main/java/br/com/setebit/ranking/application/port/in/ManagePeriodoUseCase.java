package br.com.setebit.ranking.application.port.in;

import br.com.setebit.ranking.application.dto.CriterioResponse;
import br.com.setebit.ranking.application.dto.EncontroResponse;
import br.com.setebit.ranking.application.dto.PeriodoResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ManagePeriodoUseCase {

	PeriodoResponse create(Long turmaId, String nome, LocalDate dataInicio, LocalDate dataFim);

	PeriodoResponse update(Long id, String nome, LocalDate dataInicio, LocalDate dataFim, Boolean ativo);

	PeriodoResponse getById(Long id);

	List<PeriodoResponse> listByTurma(Long turmaId);

	List<EncontroResponse> createEncontros(Long periodoId, int quantidade);

	List<EncontroResponse> listEncontros(Long periodoId);

	CriterioResponse createCriterio(Long periodoId, String nome, Integer ordem, BigDecimal pontuacaoMaxima);

	CriterioResponse updateCriterio(Long id, String nome, Integer ordem, BigDecimal pontuacaoMaxima);

	List<CriterioResponse> listCriterios(Long periodoId);
}
