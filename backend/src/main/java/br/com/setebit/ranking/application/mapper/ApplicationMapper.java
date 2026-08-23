package br.com.setebit.ranking.application.mapper;

import br.com.setebit.ranking.application.dto.AlunoResponse;
import br.com.setebit.ranking.application.dto.CriterioResponse;
import br.com.setebit.ranking.application.dto.EncontroResponse;
import br.com.setebit.ranking.application.dto.NotaResponse;
import br.com.setebit.ranking.application.dto.PeriodoResponse;
import br.com.setebit.ranking.application.dto.TurmaResponse;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Periodo;
import br.com.setebit.ranking.domain.model.Turma;

public final class ApplicationMapper {

	private ApplicationMapper() {
	}

	public static TurmaResponse toResponse(Turma turma) {
		return new TurmaResponse(
				turma.getId(),
				turma.getNome(),
				turma.getDescricao(),
				turma.isAtivo(),
				turma.getCreatedAt(),
				turma.getUpdatedAt());
	}

	public static AlunoResponse toResponse(Aluno aluno) {
		return new AlunoResponse(
				aluno.getId(),
				aluno.getTurmaId(),
				aluno.getNome(),
				aluno.getDataNascimento(),
				aluno.isAtivo(),
				aluno.getCreatedAt(),
				aluno.getUpdatedAt());
	}

	public static PeriodoResponse toResponse(Periodo periodo) {
		return new PeriodoResponse(
				periodo.getId(),
				periodo.getTurmaId(),
				periodo.getNome(),
				periodo.getDataInicio(),
				periodo.getDataFim(),
				periodo.isAtivo(),
				periodo.getCreatedAt(),
				periodo.getUpdatedAt());
	}

	public static EncontroResponse toResponse(Encontro encontro) {
		return new EncontroResponse(
				encontro.getId(),
				encontro.getPeriodoId(),
				encontro.getNumero(),
				encontro.getData(),
				encontro.getTitulo());
	}

	public static CriterioResponse toResponse(Criterio criterio) {
		return new CriterioResponse(
				criterio.getId(),
				criterio.getPeriodoId(),
				criterio.getNome(),
				criterio.getOrdem(),
				criterio.getPontuacaoMaxima());
	}

	public static NotaResponse toResponse(Nota nota) {
		return new NotaResponse(
				nota.getId(),
				nota.getAlunoId(),
				nota.getEncontroId(),
				nota.getCriterioId(),
				nota.getValor(),
				nota.getObservacao(),
				nota.getLancadoEm(),
				nota.getAtualizadoEm());
	}
}
