package br.com.setebit.ranking.application.usecase;

import br.com.setebit.ranking.application.dto.AlunoResponse;
import br.com.setebit.ranking.application.mapper.ApplicationMapper;
import br.com.setebit.ranking.application.port.in.ManageAlunoUseCase;
import br.com.setebit.ranking.application.port.out.AlunoRepositoryPort;
import br.com.setebit.ranking.application.port.out.NotaRepositoryPort;
import br.com.setebit.ranking.application.port.out.TurmaRepositoryPort;
import br.com.setebit.ranking.domain.exception.ResourceNotFoundException;
import br.com.setebit.ranking.domain.model.Aluno;
import br.com.setebit.ranking.domain.model.Turma;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManageAlunoUseCaseImpl implements ManageAlunoUseCase {

	private final AlunoRepositoryPort alunoRepository;
	private final TurmaRepositoryPort turmaRepository;
	private final NotaRepositoryPort notaRepository;

	public ManageAlunoUseCaseImpl(
			AlunoRepositoryPort alunoRepository,
			TurmaRepositoryPort turmaRepository,
			NotaRepositoryPort notaRepository) {
		this.alunoRepository = alunoRepository;
		this.turmaRepository = turmaRepository;
		this.notaRepository = notaRepository;
	}

	@Override
	public AlunoResponse create(Long turmaId, Integer numero, String nome, LocalDate dataNascimento) {
		requireTurma(turmaId);
		Instant now = Instant.now();
		Aluno aluno = new Aluno();
		aluno.setTurmaId(turmaId);
		aluno.setNumero(numero);
		aluno.setNome(nome.trim());
		aluno.setDataNascimento(dataNascimento);
		aluno.setAtivo(true);
		aluno.setCreatedAt(now);
		aluno.setUpdatedAt(now);
		return ApplicationMapper.toResponse(alunoRepository.save(aluno));
	}

	@Override
	public AlunoResponse update(Long id, Integer numero, String nome, LocalDate dataNascimento) {
		Aluno aluno = requireAluno(id);
		if (numero != null) {
			aluno.setNumero(numero);
		}
		aluno.setNome(nome.trim());
		aluno.setDataNascimento(dataNascimento);
		aluno.setUpdatedAt(Instant.now());
		return ApplicationMapper.toResponse(alunoRepository.save(aluno));
	}

	@Override
	public void delete(Long id) {
		requireAluno(id);
		notaRepository.deleteByAlunoId(id);
		alunoRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public AlunoResponse getById(Long id) {
		return ApplicationMapper.toResponse(requireAluno(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<AlunoResponse> listAll() {
		return alunoRepository.findAll().stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<AlunoResponse> listByTurma(Long turmaId) {
		requireTurma(turmaId);
		return alunoRepository.findByTurmaId(turmaId).stream().map(ApplicationMapper::toResponse).toList();
	}

	@Override
	public AlunoResponse setAtivo(Long id, boolean ativo) {
		Aluno aluno = requireAluno(id);
		aluno.setAtivo(ativo);
		aluno.setUpdatedAt(Instant.now());
		return ApplicationMapper.toResponse(alunoRepository.save(aluno));
	}

	private void requireTurma(Long turmaId) {
		turmaRepository.findById(turmaId)
				.filter(Turma::isAtivo)
				.orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada: " + turmaId));
	}

	private Aluno requireAluno(Long id) {
		return alunoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + id));
	}
}
