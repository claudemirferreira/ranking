package br.com.setebit.ranking.application.port.in;

import br.com.setebit.ranking.application.dto.RankingItemResponse;
import java.util.List;

public interface GetRankingUseCase {

	List<RankingItemResponse> execute(Long periodoId);
}
