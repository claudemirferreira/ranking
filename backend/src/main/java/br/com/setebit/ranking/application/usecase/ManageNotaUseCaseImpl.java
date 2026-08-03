package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.HistoricoAlunoResponse;
import br.com.setebit.ranking.application.dto.NotaResponse;
import br.com.setebit.ranking.application.mapper.ApplicationMapper;
import br.com.setebit.ranking.application.port.in.ManageNotaUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.application.port.out.EncontroRepositoryPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.application.port.out.PeriodoRepositoryPort;
import br.com.setebit.ranking.domain.exception.BusinessRuleException;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Periodo;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManageNotaUseCaseImpl implements ManageNotaUseCase {

	private final NotaRepositoryPort notaRepository;
	private final AlunoRepositoryPort alunoRepository;
	private final EncontroRepositoryPort encontroRepository;
	private final CriterioRepositoryPort criterioRepository;
	private final PeriodoRepositoryPort periodoRepository;

	public ManageNotaUseCaseImpl(
			NotaRepositoryPort notaRepository,
			AlunoRepositoryPort alunoRepository,
			EncontroRepositoryPort encontroRepository,
			CriterioRepositoryPort criterioRepository,
			PeriodoRepositoryPort periodoRepository) {
		this.notaRepository = notaRepository;
		this.alunoRepository = alunoRepository;
		this.encontroRepository = encontroRepository;
		this.criterioRepository = criterioRepository;
		this.periodoRepository = periodoRepository;
	}

	@Override
	public NotaResponse upsert(Long alunoId, Long encontroId, Long criterioId, BigDecimal valor, String observacao) {
		return ApplicationMapper.toResponse(upsertInternal(alunoId, encontroId, criterioId, valor, observacao));
	}

	@Override
	public List<NotaResponse> upsertLote(List<NotaItem> itens) {
		List<Nota> salvas = new ArrayList<>();
		for (NotaItem item : itens) {
			salvas.add(upsertInternal(item.alunoId(), item.encontroId(), item.criterioId(), item.valor(), item.observacao()));
		}
		return salvas.stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<NotaResponse> listByPeriodo(Long periodoId, Long encontroId, Long criterioId) {
		requirePeriodo(periodoId);
		return notaRepository.findByPeriodoIdFiltered(periodoId, encontroId, criterioId).stream()
				.map(ApplicationMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public HistoricoAlunoResponse historico(Long alunoId, Long periodoId) {
		Aluno aluno = requireAluno(alunoId);
		requirePeriodo(periodoId);
		List<Nota> notas = notaRepository.findByAlunoIdAndPeriodoId(alunoId, periodoId);
		BigDecimal total = notas.stream().map(Nota::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
		return new HistoricoAlunoResponse(
				aluno.getId(),
				aluno.getNome(),
				periodoId,
				total,
				notas.stream().map(ApplicationMapper::toResponse).toList());
	}

	private Nota upsertInternal(Long alunoId, Long encontroId, Long criterioId, BigDecimal valor, String observacao) {
		Aluno aluno = requireAluno(alunoId);
		Encontro encontro = encontroRepository.findById(encontroId)
				.orElseThrow(() -> new ResourceNotFoundException("Encontro não encontrado: " + encontroId));
		Criterio criterio = criterioRepository.findById(criterioId)
				.orElseThrow(() -> new ResourceNotFoundException("Critério não encontrado: " + criterioId));

		if (!encontro.getPeriodoId().equals(criterio.getPeriodoId())) {
			throw new BusinessRuleException("Encontro e critério devem pertencer ao mesmo período");
		}

		Periodo periodo = requirePeriodo(encontro.getPeriodoId());
		if (!aluno.getTurmaId().equals(periodo.getTurmaId())) {
			throw new BusinessRuleException("Aluno não pertence à turma do período");
		}

		if (valor.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessRuleException("Valor da nota não pode ser negativo");
		}
		if (valor.compareTo(criterio.getPontuacaoMaxima()) > 0) {
			throw new BusinessRuleException(
					"Valor da nota excede a pontuação máxima (" + criterio.getPontuacaoMaxima() + ")");
		}

		Instant now = Instant.now();
		Nota nota = notaRepository
				.findByAlunoIdAndEncontroIdAndCriterioId(alunoId, encontroId, criterioId)
				.orElseGet(Nota::new);

		if (nota.getId() == null) {
			nota.setAlunoId(alunoId);
			nota.setEncontroId(encontroId);
			nota.setCriterioId(criterioId);
			nota.setLancadoEm(now);
		}
		nota.setValor(valor);
		nota.setObservacao(observacao);
		nota.setAtualizadoEm(now);
		return notaRepository.save(nota);
	}

	private Aluno requireAluno(Long id) {
		return alunoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + id));
	}

	private Periodo requirePeriodo(Long id) {
		return periodoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Período não encontrado: " + id));
	}
}
