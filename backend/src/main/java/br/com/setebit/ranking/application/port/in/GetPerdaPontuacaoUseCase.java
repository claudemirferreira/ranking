package br.com.setebit.ranking.application.port.in;

import br.com.setebit.ranking.application.dto.PerdaPontuacaoResponse;

public interface GetPerdaPontuacaoUseCase {

	PerdaPontuacaoResponse execute(Long periodoId);
}
