package br.com.setebit.ranking.infrastructure.persistence.mapper;

import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Periodo;
import br.com.setebit.ranking.domain.model.Turma;
import br.com.setebit.ranking.infrastructure.persistence.entity.AlunoJpaEntity;
import br.com.setebit.ranking.infrastructure.persistence.entity.CriterioJpaEntity;
import br.com.setebit.ranking.infrastructure.persistence.entity.EncontroJpaEntity;
import br.com.setebit.ranking.infrastructure.persistence.entity.NotaJpaEntity;
import br.com.setebit.ranking.infrastructure.persistence.entity.PeriodoJpaEntity;
import br.com.setebit.ranking.infrastructure.persistence.entity.TurmaJpaEntity;

public final class JpaMapper {

	private JpaMapper() {
	}

	public static Turma toDomain(TurmaJpaEntity entity) {
		Turma turma = new Turma();
		turma.setId(entity.getId());
		turma.setNome(entity.getNome());
		turma.setDescricao(entity.getDescricao());
		turma.setAtivo(entity.isAtivo());
		turma.setCreatedAt(entity.getCreatedAt());
		turma.setUpdatedAt(entity.getUpdatedAt());
		turma.setExcluidoEm(entity.getExcluidoEm());
		return turma;
	}

	public static TurmaJpaEntity toEntity(Turma domain) {
		TurmaJpaEntity entity = new TurmaJpaEntity();
		entity.setId(domain.getId());
		entity.setNome(domain.getNome());
		entity.setDescricao(domain.getDescricao());
		entity.setAtivo(domain.isAtivo());
		entity.setCreatedAt(domain.getCreatedAt());
		entity.setUpdatedAt(domain.getUpdatedAt());
		entity.setExcluidoEm(domain.getExcluidoEm());
		return entity;
	}

	public static Aluno toDomain(AlunoJpaEntity entity) {
		Aluno aluno = new Aluno();
		aluno.setId(entity.getId());
		aluno.setTurmaId(entity.getTurmaId());
		aluno.setNumero(entity.getNumero());
		aluno.setNome(entity.getNome());
		aluno.setDataNascimento(entity.getDataNascimento());
		aluno.setAtivo(entity.isAtivo());
		aluno.setCreatedAt(entity.getCreatedAt());
		aluno.setUpdatedAt(entity.getUpdatedAt());
		return aluno;
	}

	public static AlunoJpaEntity toEntity(Aluno domain) {
		AlunoJpaEntity entity = new AlunoJpaEntity();
		entity.setId(domain.getId());
		entity.setTurmaId(domain.getTurmaId());
		entity.setNumero(domain.getNumero());
		entity.setNome(domain.getNome());
		entity.setDataNascimento(domain.getDataNascimento());
		entity.setAtivo(domain.isAtivo());
		entity.setCreatedAt(domain.getCreatedAt());
		entity.setUpdatedAt(domain.getUpdatedAt());
		return entity;
	}

	public static Periodo toDomain(PeriodoJpaEntity entity) {
		Periodo periodo = new Periodo();
		periodo.setId(entity.getId());
		periodo.setTurmaId(entity.getTurmaId());
		periodo.setNome(entity.getNome());
		periodo.setDataInicio(entity.getDataInicio());
		periodo.setDataFim(entity.getDataFim());
		periodo.setAtivo(entity.isAtivo());
		periodo.setCreatedAt(entity.getCreatedAt());
		periodo.setUpdatedAt(entity.getUpdatedAt());
		return periodo;
	}

	public static PeriodoJpaEntity toEntity(Periodo domain) {
		PeriodoJpaEntity entity = new PeriodoJpaEntity();
		entity.setId(domain.getId());
		entity.setTurmaId(domain.getTurmaId());
		entity.setNome(domain.getNome());
		entity.setDataInicio(domain.getDataInicio());
		entity.setDataFim(domain.getDataFim());
		entity.setAtivo(domain.isAtivo());
		entity.setCreatedAt(domain.getCreatedAt());
		entity.setUpdatedAt(domain.getUpdatedAt());
		return entity;
	}

	public static Encontro toDomain(EncontroJpaEntity entity) {
		Encontro encontro = new Encontro();
		encontro.setId(entity.getId());
		encontro.setPeriodoId(entity.getPeriodoId());
		encontro.setNumero(entity.getNumero());
		encontro.setData(entity.getData());
		encontro.setTitulo(entity.getTitulo());
		return encontro;
	}

	public static EncontroJpaEntity toEntity(Encontro domain) {
		EncontroJpaEntity entity = new EncontroJpaEntity();
		entity.setId(domain.getId());
		entity.setPeriodoId(domain.getPeriodoId());
		entity.setNumero(domain.getNumero());
		entity.setData(domain.getData());
		entity.setTitulo(domain.getTitulo());
		return entity;
	}

	public static Criterio toDomain(CriterioJpaEntity entity) {
		Criterio criterio = new Criterio();
		criterio.setId(entity.getId());
		criterio.setPeriodoId(entity.getPeriodoId());
		criterio.setNome(entity.getNome());
		criterio.setOrdem(entity.getOrdem());
		criterio.setPontuacaoMaxima(entity.getPontuacaoMaxima());
		return criterio;
	}

	public static CriterioJpaEntity toEntity(Criterio domain) {
		CriterioJpaEntity entity = new CriterioJpaEntity();
		entity.setId(domain.getId());
		entity.setPeriodoId(domain.getPeriodoId());
		entity.setNome(domain.getNome());
		entity.setOrdem(domain.getOrdem());
		entity.setPontuacaoMaxima(domain.getPontuacaoMaxima());
		return entity;
	}

	public static Nota toDomain(NotaJpaEntity entity) {
		Nota nota = new Nota();
		nota.setId(entity.getId());
		nota.setAlunoId(entity.getAlunoId());
		nota.setEncontroId(entity.getEncontroId());
		nota.setCriterioId(entity.getCriterioId());
		nota.setValor(entity.getValor());
		nota.setObservacao(entity.getObservacao());
		nota.setLancadoEm(entity.getLancadoEm());
		nota.setAtualizadoEm(entity.getAtualizadoEm());
		return nota;
	}

	public static NotaJpaEntity toEntity(Nota domain) {
		NotaJpaEntity entity = new NotaJpaEntity();
		entity.setId(domain.getId());
		entity.setAlunoId(domain.getAlunoId());
		entity.setEncontroId(domain.getEncontroId());
		entity.setCriterioId(domain.getCriterioId());
		entity.setValor(domain.getValor());
		entity.setObservacao(domain.getObservacao());
		entity.setLancadoEm(domain.getLancadoEm());
		entity.setAtualizadoEm(domain.getAtualizadoEm());
		return entity;
	}
}
