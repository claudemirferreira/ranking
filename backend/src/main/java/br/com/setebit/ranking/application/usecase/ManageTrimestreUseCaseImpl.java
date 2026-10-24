package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.CriterioResponse;
import br.com.setebit.ranking.application.dto.SabadoResponse;
import br.com.setebit.ranking.application.dto.TrimestreResponse;
import br.com.setebit.ranking.application.mapper.ApplicationMapper;
import br.com.setebit.ranking.application.port.in.ManageTrimestreUseCase;
import br.com.setebit.ranking.application.port.out.CriterioRepositoryPort;
import br.com.setebit.ranking.application.port.out.SabadoRepositoryPort;
import br.com.setebit.ranking.application.port.out.TrimestreRepositoryPort;
import br.com.setebit.ranking.application.port.out.TurmaRepositoryPort;
import br.com.setebit.ranking.domain.exception.BusinessRuleException;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Criterio;
import br.com.setebit.ranking.domain.model.Sabado;
import br.com.setebit.ranking.domain.model.Trimestre;
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
public class ManageTrimestreUseCaseImpl implements ManageTrimestreUseCase {

	private final TrimestreRepositoryPort trimestreRepository;
	private final TurmaRepositoryPort turmaRepository;
	private final SabadoRepositoryPort sabadoRepository;
	private final CriterioRepositoryPort criterioRepository;

	public ManageTrimestreUseCaseImpl(
			TrimestreRepositoryPort trimestreRepository,
			TurmaRepositoryPort turmaRepository,
			SabadoRepositoryPort sabadoRepository,
			CriterioRepositoryPort criterioRepository) {
		this.trimestreRepository = trimestreRepository;
		this.turmaRepository = turmaRepository;
		this.sabadoRepository = sabadoRepository;
		this.criterioRepository = criterioRepository;
	}

	@Override
	public TrimestreResponse create(Long turmaId, String nome, int ano, LocalDate dataInicio, LocalDate dataFim) {
		requireTurma(turmaId);
		requireAno(ano);
		String nomeLimpo = nome.trim();
		requireNomeLivre(turmaId, ano, nomeLimpo, null);
		Instant now = Instant.now();
		Trimestre trimestre = new Trimestre();
		trimestre.setTurmaId(turmaId);
		trimestre.setNome(nomeLimpo);
		trimestre.setAno(ano);
		trimestre.setDataInicio(dataInicio);
		trimestre.setDataFim(dataFim);
		trimestre.setAtivo(true);
		trimestre.setCreatedAt(now);
		trimestre.setUpdatedAt(now);
		return ApplicationMapper.toResponse(trimestreRepository.save(trimestre));
	}

	@Override
	public TrimestreResponse update(Long id, String nome, int ano, LocalDate dataInicio, LocalDate dataFim, Boolean ativo) {
		Trimestre trimestre = requireTrimestre(id);
		requireAno(ano);
		String nomeLimpo = nome.trim();
		requireNomeLivre(trimestre.getTurmaId(), ano, nomeLimpo, id);
		trimestre.setNome(nomeLimpo);
		trimestre.setAno(ano);
		trimestre.setDataInicio(dataInicio);
		trimestre.setDataFim(dataFim);
		if (ativo != null) {
			trimestre.setAtivo(ativo);
		}
		trimestre.setUpdatedAt(Instant.now());
		return ApplicationMapper.toResponse(trimestreRepository.save(trimestre));
	}

	@Override
	@Transactional(readOnly = true)
	public TrimestreResponse getById(Long id) {
		return ApplicationMapper.toResponse(requireTrimestre(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<TrimestreResponse> listByTurma(Long turmaId) {
		requireTurma(turmaId);
		return trimestreRepository.findByTurmaId(turmaId).stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	public List<SabadoResponse> createSabados(Long trimestreId, int quantidade) {
		if (quantidade < 1) {
			throw new BusinessRuleException("Quantidade de sábados deve ser pelo menos 1");
		}
		requireTrimestre(trimestreId);
		int existentes = sabadoRepository.countByTrimestreId(trimestreId);
		List<Sabado> novos = new ArrayList<>();
		for (int i = 1; i <= quantidade; i++) {
			int numero = existentes + i;
			Sabado sabado = new Sabado();
			sabado.setTrimestreId(trimestreId);
			sabado.setNumero(numero);
			sabado.setTitulo("Sábado " + numero);
			novos.add(sabado);
		}
		return sabadoRepository.saveAll(novos).stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<SabadoResponse> listSabados(Long trimestreId) {
		requireTrimestre(trimestreId);
		return sabadoRepository.findByTrimestreId(trimestreId).stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	public CriterioResponse createCriterio(Long trimestreId, String nome, Integer ordem, BigDecimal pontuacaoMaxima) {
		requireTrimestre(trimestreId);
		Criterio criterio = new Criterio();
		criterio.setTrimestreId(trimestreId);
		criterio.setNome(nome.trim());
		criterio.setOrdem(ordem != null ? ordem : criterioRepository.findByTrimestreId(trimestreId).size() + 1);
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
	public List<CriterioResponse> listCriterios(Long trimestreId) {
		requireTrimestre(trimestreId);
		return criterioRepository.findByTrimestreId(trimestreId).stream().map(ApplicationMapper::toResponse).toList();
	}

	private void requireTurma(Long turmaId) {
		turmaRepository.findById(turmaId)
				.filter(Turma::isAtivo)
				.orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada: " + turmaId));
	}

	private Trimestre requireTrimestre(Long id) {
		return trimestreRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Trimestre não encontrado: " + id));
	}

	private void requireAno(int ano) {
		if (ano < 2000 || ano > 2100) {
			throw new BusinessRuleException("Ano do trimestre deve estar entre 2000 e 2100");
		}
	}

	private void requireNomeLivre(Long turmaId, int ano, String nome, Long ignoreId) {
		trimestreRepository.findByTurmaIdAndAnoAndNomeIgnoreCase(turmaId, ano, nome)
				.filter(found -> ignoreId == null || !ignoreId.equals(found.getId()))
				.ifPresent(found -> {
					throw new BusinessRuleException(
							"Já existe o trimestre \"" + nome + "\" em " + ano + " nesta turma");
				});
	}
}
