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
						"Luísa",
						"Júlia",
						"Sofhia",
						"Adrielly",
						"Lorena",
						"Valentina",
						"Esther",
						"João",
						"Isaque",
						"João Levi",
						"Vitória",
						"Manuela",
						"João Pedro",
						"João Lucas",
						"João Arthur")
				.forEach(nome -> manageAlunoUseCase.create(turma.id(), nome, null));

		var periodo = managePeriodoUseCase.create(turma.id(), "Lição 3", null, null);
		managePeriodoUseCase.createEncontros(periodo.id(), 13);
		managePeriodoUseCase.createCriterio(periodo.id(), "Critério A", 1, BigDecimal.TEN);
		managePeriodoUseCase.createCriterio(periodo.id(), "Critério B", 2, BigDecimal.TEN);
		managePeriodoUseCase.createCriterio(periodo.id(), "Critério C", 3, BigDecimal.TEN);

		log.info("Seed concluído: turma Juvenis, 15 alunos, período com 13 sábados e 3 critérios");
	}
}
