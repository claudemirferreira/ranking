package br.com.setebit.ranking.application.port.in;

import br.com.setebit.ranking.application.dto.EvolucaoPontuacaoResponse;

public interface GetEvolucaoPontuacaoUseCase {

	EvolucaoPontuacaoResponse execute(Long periodoId);
}
