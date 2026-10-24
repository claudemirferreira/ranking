package br.com.setebit.ranking.infrastructure.persistence.mapper;

import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Sabado;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Trimestre;
import br.com.setebit.ranking.domain.model.Turma;
import br.com.setebit.ranking.infrastructure.persistence.entity.AlunoJpaEntity;
import br.com.setebit.ranking.infrastructure.persistence.entity.CriterioJpaEntity;
import br.com.setebit.ranking.infrastructure.persistence.entity.SabadoJpaEntity;
import br.com.setebit.ranking.infrastructure.persistence.entity.NotaJpaEntity;
import br.com.setebit.ranking.infrastructure.persistence.entity.TrimestreJpaEntity;
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

	public static Trimestre toDomain(TrimestreJpaEntity entity) {
		Trimestre trimestre = new Trimestre();
		trimestre.setId(entity.getId());
		trimestre.setTurmaId(entity.getTurmaId());
		trimestre.setNome(entity.getNome());
		trimestre.setDataInicio(entity.getDataInicio());
		trimestre.setDataFim(entity.getDataFim());
		trimestre.setAtivo(entity.isAtivo());
		trimestre.setCreatedAt(entity.getCreatedAt());
		trimestre.setUpdatedAt(entity.getUpdatedAt());
		return trimestre;
	}

	public static TrimestreJpaEntity toEntity(Trimestre domain) {
		TrimestreJpaEntity entity = new TrimestreJpaEntity();
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

	public static Sabado toDomain(SabadoJpaEntity entity) {
		Sabado sabado = new Sabado();
		sabado.setId(entity.getId());
		sabado.setTrimestreId(entity.getTrimestreId());
		sabado.setNumero(entity.getNumero());
		sabado.setData(entity.getData());
		sabado.setTitulo(entity.getTitulo());
		return sabado;
	}

	public static SabadoJpaEntity toEntity(Sabado domain) {
		SabadoJpaEntity entity = new SabadoJpaEntity();
		entity.setId(domain.getId());
		entity.setTrimestreId(domain.getTrimestreId());
		entity.setNumero(domain.getNumero());
		entity.setData(domain.getData());
		entity.setTitulo(domain.getTitulo());
		return entity;
	}

	public static Criterio toDomain(CriterioJpaEntity entity) {
		Criterio criterio = new Criterio();
		criterio.setId(entity.getId());
		criterio.setTrimestreId(entity.getTrimestreId());
		criterio.setNome(entity.getNome());
		criterio.setOrdem(entity.getOrdem());
		criterio.setPontuacaoMaxima(entity.getPontuacaoMaxima());
		return criterio;
	}

	public static CriterioJpaEntity toEntity(Criterio domain) {
		CriterioJpaEntity entity = new CriterioJpaEntity();
		entity.setId(domain.getId());
		entity.setTrimestreId(domain.getTrimestreId());
		entity.setNome(domain.getNome());
		entity.setOrdem(domain.getOrdem());
		entity.setPontuacaoMaxima(domain.getPontuacaoMaxima());
		return entity;
	}

	public static Nota toDomain(NotaJpaEntity entity) {
		Nota nota = new Nota();
		nota.setId(entity.getId());
		nota.setAlunoId(entity.getAlunoId());
		nota.setSabadoId(entity.getSabadoId());
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
		entity.setSabadoId(domain.getSabadoId());
		entity.setCriterioId(domain.getCriterioId());
		entity.setValor(domain.getValor());
		entity.setObservacao(domain.getObservacao());
		entity.setLancadoEm(domain.getLancadoEm());
		entity.setAtualizadoEm(domain.getAtualizadoEm());
		return entity;
	}
}
