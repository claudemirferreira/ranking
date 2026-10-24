package br.com.setebit.ranking.application.port.in;

public interface ExportRankingPdfUseCase {

	byte[] execute(Long trimestreId);
}
