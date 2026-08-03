package br.com.setebit.ranking.infrastructure.persistence.repository;

import br.com.setebit.ranking.infrastructure.persistence.entity.AlunoJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAlunoRepository extends JpaRepository<AlunoJpaEntity, Long> {

	List<AlunoJpaEntity> findAllByOrderByTurmaIdAscNumeroAscNomeAsc();

	List<AlunoJpaEntity> findByTurmaIdOrderByNumeroAscNomeAsc(Long turmaId);

	List<AlunoJpaEntity> findByTurmaIdAndAtivoTrueOrderByNumeroAscNomeAsc(Long turmaId);
}
