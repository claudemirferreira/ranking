package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.CriterioResponse;
import br.com.setebit.ranking.application.dto.EncontroResponse;
import br.com.setebit.ranking.application.dto.PeriodoResponse;
import br.com.setebit.ranking.application.mapper.ApplicationMapper;
import br.com.setebit.ranking.application.port.in.ManagePeriodoUseCase;
import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.application.port.out.EncontroRepositoryPort;
import br.com.setebit.ranking.application.port.out.PeriodoRepositoryPort;
import br.com.setebit.ranking.application.port.out.TurmaRepositoryPort;
import br.com.setebit.ranking.domain.exception.BusinessRuleException;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Encontro;
import br.com.setebit.ranking.domain.model.Periodo;
import br.com.setebit.ranking.domain.model.Turma;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManagePeriodoUseCaseImpl implements ManagePeriodoUseCase {

	private final PeriodoRepositoryPort periodoRepository;
	private final TurmaRepositoryPort turmaRepository;
	private final EncontroRepositoryPort encontroRepository;
	private final CriterioRepositoryPort criterioRepository;

	public ManagePeriodoUseCaseImpl(
			PeriodoRepositoryPort periodoRepository,
			TurmaRepositoryPort turmaRepository,
			EncontroRepositoryPort encontroRepository,
			CriterioRepositoryPort criterioRepository) {
		this.periodoRepository = periodoRepository;
		this.turmaRepository = turmaRepository;
		this.encontroRepository = encontroRepository;
		this.criterioRepository = criterioRepository;
	}

	@Override
	public PeriodoResponse create(Long turmaId, String nome, LocalDate dataInicio, LocalDate dataFim) {
		requireTurma(turmaId);
		Instant now = Instant.now();
		Periodo periodo = new Periodo();
		periodo.setTurmaId(turmaId);
		periodo.setNome(nome.trim());
		periodo.setDataInicio(dataInicio);
		periodo.setDataFim(dataFim);
		periodo.setAtivo(true);
		periodo.setCreatedAt(now);
		periodo.setUpdatedAt(now);
		return ApplicationMapper.toResponse(periodoRepository.save(periodo));
	}

	@Override
	public PeriodoResponse update(Long id, String nome, LocalDate dataInicio, LocalDate dataFim, Boolean ativo) {
		Periodo periodo = requirePeriodo(id);
		periodo.setNome(nome.trim());
		periodo.setDataInicio(dataInicio);
		periodo.setDataFim(dataFim);
		if (ativo != null) {
			periodo.setAtivo(ativo);
		}
		periodo.setUpdatedAt(Instant.now());
		return ApplicationMapper.toResponse(periodoRepository.save(periodo));
	}

	@Override
	@Transactional(readOnly = true)
	public PeriodoResponse getById(Long id) {
		return ApplicationMapper.toResponse(requirePeriodo(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<PeriodoResponse> listByTurma(Long turmaId) {
		requireTurma(turmaId);
		return periodoRepository.findByTurmaId(turmaId).stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	public List<EncontroResponse> createEncontros(Long periodoId, int quantidade) {
		if (quantidade < 1) {
			throw new BusinessRuleException("Quantidade de encontros deve ser pelo menos 1");
		}
		requirePeriodo(periodoId);
		int existentes = encontroRepository.countByPeriodoId(periodoId);
		List<Encontro> novos = new ArrayList<>();
		for (int i = 1; i <= quantidade; i++) {
			int numero = existentes + i;
			Encontro encontro = new Encontro();
			encontro.setPeriodoId(periodoId);
			encontro.setNumero(numero);
			encontro.setTitulo("Sábado " + numero);
			novos.add(encontro);
		}
		return encontroRepository.saveAll(novos).stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<EncontroResponse> listEncontros(Long periodoId) {
		requirePeriodo(periodoId);
		return encontroRepository.findByPeriodoId(periodoId).stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	public CriterioResponse createCriterio(Long periodoId, String nome, Integer ordem, BigDecimal pontuacaoMaxima) {
		requirePeriodo(periodoId);
		Criterio criterio = new Criterio();
		criterio.setPeriodoId(periodoId);
		criterio.setNome(nome.trim());
		criterio.setOrdem(ordem != null ? ordem : criterioRepository.findByPeriodoId(periodoId).size() + 1);
		criterio.setPontuacaoMaxima(pontuacaoMaxima != null ? pontuacaoMaxima : BigDecimal.TEN);
		return ApplicationMapper.toResponse(criterioRepository.save(criterio));
	}

	@Override
	public CriterioResponse updateCriterio(Long id, String nome, Integer ordem, BigDecimal pontuacaoMaxima) {
		Criterio criterio = criterioRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Critério não encontrado: " + id));
		criterio.setNome(nome.trim());
		if (ordem != null) {
			criterio.setOrdem(ordem);
		}
		if (pontuacaoMaxima != null) {
			criterio.setPontuacaoMaxima(pontuacaoMaxima);
		}
		return ApplicationMapper.toResponse(criterioRepository.save(criterio));
	}

	@Override
	@Transactional(readOnly = true)
	public List<CriterioResponse> listCriterios(Long periodoId) {
		requirePeriodo(periodoId);
		return criterioRepository.findByPeriodoId(periodoId).stream().map(ApplicationMapper::toResponse).toList();
	}

	private void requireTurma(Long turmaId) {
		turmaRepository.findById(turmaId)
				.filter(Turma::isAtivo)
				.orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada: " + turmaId));
	}

	private Periodo requirePeriodo(Long id) {
		return periodoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Período não encontrado: " + id));
	}
}
