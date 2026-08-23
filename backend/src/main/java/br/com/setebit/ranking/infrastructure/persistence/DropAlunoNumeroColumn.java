package br.com.setebit.ranking.infrastructure.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class DropAlunoNumeroColumn implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(DropAlunoNumeroColumn.class);

	private final JdbcTemplate jdbcTemplate;

	public DropAlunoNumeroColumn(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void run(ApplicationArguments args) {
		try {
			jdbcTemplate.execute("ALTER TABLE alunos DROP COLUMN numero");
			log.info("Coluna alunos.numero removida");
		}
		catch (Exception ex) {
			log.debug("Coluna alunos.numero já inexistente: {}", ex.getMessage());
		}
	}
}
