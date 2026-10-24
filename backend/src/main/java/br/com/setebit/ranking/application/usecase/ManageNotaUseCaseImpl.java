package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.HistoricoAlunoResponse;
import br.com.setebit.ranking.application.dto.NotaResponse;
import br.com.setebit.ranking.application.mapper.ApplicationMapper;
import br.com.setebit.ranking.application.port.in.ManageNotaUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.application.port.out.SabadoRepositoryPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.application.port.out.TrimestreRepositoryPort;
import br.com.setebit.ranking.domain.exception.BusinessRuleException;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Sabado;
import br.com.setebit.ranking.domain.model.Nota;
import br.com.setebit.ranking.domain.model.Trimestre;
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
	private final SabadoRepositoryPort sabadoRepository;
	private final CriterioRepositoryPort criterioRepository;
	private final TrimestreRepositoryPort trimestreRepository;

	public ManageNotaUseCaseImpl(
			NotaRepositoryPort notaRepository,
			AlunoRepositoryPort alunoRepository,
			SabadoRepositoryPort sabadoRepository,
			CriterioRepositoryPort criterioRepository,
			TrimestreRepositoryPort trimestreRepository) {
		this.notaRepository = notaRepository;
		this.alunoRepository = alunoRepository;
		this.sabadoRepository = sabadoRepository;
		this.criterioRepository = criterioRepository;
		this.trimestreRepository = trimestreRepository;
	}

	@Override
	public NotaResponse upsert(Long alunoId, Long sabadoId, Long criterioId, BigDecimal valor, String observacao) {
		return ApplicationMapper.toResponse(upsertInternal(alunoId, sabadoId, criterioId, valor, observacao));
	}

	@Override
	public List<NotaResponse> upsertLote(List<NotaItem> itens) {
		List<Nota> salvas = new ArrayList<>();
		for (NotaItem item : itens) {
			salvas.add(upsertInternal(item.alunoId(), item.sabadoId(), item.criterioId(), item.valor(), item.observacao()));
		}
		return salvas.stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<NotaResponse> listByTrimestre(Long trimestreId, Long sabadoId, Long criterioId) {
		requireTrimestre(trimestreId);
		return notaRepository.findByTrimestreIdFiltered(trimestreId, sabadoId, criterioId).stream()
				.map(ApplicationMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public HistoricoAlunoResponse historico(Long alunoId, Long trimestreId) {
		Aluno aluno = requireAluno(alunoId);
		requireTrimestre(trimestreId);
		List<Nota> notas = notaRepository.findByAlunoIdAndTrimestreId(alunoId, trimestreId);
		BigDecimal total = notas.stream().map(Nota::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
		return new HistoricoAlunoResponse(
				aluno.getId(),
				aluno.getNome(),
				trimestreId,
				total,
				notas.stream().map(ApplicationMapper::toResponse).toList());
	}

	private Nota upsertInternal(Long alunoId, Long sabadoId, Long criterioId, BigDecimal valor, String observacao) {
		Aluno aluno = requireAluno(alunoId);
		Sabado sabado = sabadoRepository.findById(sabadoId)
				.orElseThrow(() -> new ResourceNotFoundException("Sábado não encontrado: " + sabadoId));
		Criterio criterio = criterioRepository.findById(criterioId)
				.orElseThrow(() -> new ResourceNotFoundException("Critério não encontrado: " + criterioId));

		if (!sabado.getTrimestreId().equals(criterio.getTrimestreId())) {
			throw new BusinessRuleException("Sábado e critério devem pertencer ao mesmo trimestre");
		}

		Trimestre trimestre = requireTrimestre(sabado.getTrimestreId());
		if (!aluno.getTurmaId().equals(trimestre.getTurmaId())) {
			throw new BusinessRuleException("Aluno não pertence à turma do trimestre");
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
				.findByAlunoIdAndSabadoIdAndCriterioId(alunoId, sabadoId, criterioId)
				.orElseGet(Nota::new);

		if (nota.getId() == null) {
			nota.setAlunoId(alunoId);
			nota.setSabadoId(sabadoId);
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

	private Trimestre requireTrimestre(Long id) {
		return trimestreRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Trimestre não encontrado: " + id));
	}
}
