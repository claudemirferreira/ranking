package br.com.setebit.ranking.infrastructure.seed;

import br.com.setebit.ranking.application.port.in.ManageAlunoUseCase;
import br.com.setebit.ranking.application.port.in.ManagePeriodoUseCase;
import br.com.setebit.ranking.application.port.in.ManageTurmaUseCase;
import br.com.setebit.ranking.application.port.out.TurmaRepositoryPort;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ranking.seed.enabled", havingValue = "true")
public class DevDataLoader implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(DevDataLoader.class);

	private final TurmaRepositoryPort turmaRepository;
	private final ManageTurmaUseCase manageTurmaUseCase;
	private final ManageAlunoUseCase manageAlunoUseCase;
	private final ManagePeriodoUseCase managePeriodoUseCase;

	public DevDataLoader(
			TurmaRepositoryPort turmaRepository,
			ManageTurmaUseCase manageTurmaUseCase,
			ManageAlunoUseCase manageAlunoUseCase,
			ManagePeriodoUseCase managePeriodoUseCase) {
		this.turmaRepository = turmaRepository;
		this.manageTurmaUseCase = manageTurmaUseCase;
		this.manageAlunoUseCase = manageAlunoUseCase;
		this.managePeriodoUseCase = managePeriodoUseCase;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (!turmaRepository.findAllActive().isEmpty()) {
			log.info("Seed ignorado: já existem turmas cadastradas");
			return;
		}

		var turma = manageTurmaUseCase.create("Juvenis", "Classe dos Juvenis — salinha");
		List.of(
						new SeedAluno(1, "Luísa"),
						new SeedAluno(2, "Júlia"),
						new SeedAluno(3, "Sofhia"),
						new SeedAluno(4, "Adrielly"),
						new SeedAluno(5, "Lorena"),
						new SeedAluno(6, "Valentina"),
						new SeedAluno(7, "Esther"),
						new SeedAluno(9, "João"),
						new SeedAluno(10, "Isaque"),
						new SeedAluno(11, "João Levi"),
						new SeedAluno(12, "Vitória"),
						new SeedAluno(13, "Manuela"),
						new SeedAluno(14, "João Pedro"),
						new SeedAluno(15, "João Lucas"),
						new SeedAluno(22, "João Arthur"))
				.forEach(a -> manageAlunoUseCase.create(turma.id(), a.numero(), a.nome()));

		var periodo = managePeriodoUseCase.create(turma.id(), "Lição 3", null, null);
		managePeriodoUseCase.createEncontros(periodo.id(), 13);
		managePeriodoUseCase.createCriterio(periodo.id(), "Critério A", 1, BigDecimal.TEN);
		managePeriodoUseCase.createCriterio(periodo.id(), "Critério B", 2, BigDecimal.TEN);
		managePeriodoUseCase.createCriterio(periodo.id(), "Critério C", 3, BigDecimal.TEN);

		log.info("Seed concluído: turma Juvenis, 15 alunos, período com 13 sábados e 3 critérios");
	}

	private record SeedAluno(int numero, String nome) {
	}
}
