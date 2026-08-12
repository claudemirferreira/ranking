package br.com.setebit.ranking.application.port.out;

import br.com.setebit.ranking.application.dto.LancamentoPdfRow;
import java.util.List;
import java.util.Map;

public interface LancamentoPdfReportPort {

	byte[] generate(Map<String, Object> parameters, List<LancamentoPdfRow> rows);
}
