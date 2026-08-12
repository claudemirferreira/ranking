package br.com.setebit.ranking.application.port.in;

public interface ExportLancamentoPdfUseCase {

	byte[] execute(Long periodoId, Long encontroId);
}
