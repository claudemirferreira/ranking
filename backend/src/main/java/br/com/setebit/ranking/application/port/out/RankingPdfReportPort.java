package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.application.dto.RankingPdfRow;
import java.util.List;
import java.util.Map;

public interface RankingPdfReportPort {

	byte[] generate(Map<String, Object> parameters, List<RankingPdfRow> rows);
}
