package br.com.setebit.ranking.application.port.in;

import br.com.setebit.ranking.application.dto.HistoricoAlunoResponse;
import br.com.setebit.ranking.application.dto.NotaResponse;
import java.math.BigDecimal;
import java.util.List;

public interface ManageNotaUseCase {

	NotaResponse upsert(Long alunoId, Long encontroId, Long criterioId, BigDecimal valor, String observacao);

	List<NotaResponse> upsertLote(List<NotaItem> itens);

	List<NotaResponse> listByPeriodo(Long periodoId, Long encontroId, Long criterioId);

	HistoricoAlunoResponse historico(Long alunoId, Long periodoId);

	record NotaItem(Long alunoId, Long encontroId, Long criterioId, BigDecimal valor, String observacao) {
	}
}
