package br.com.setebit.ranking.infrastructure.persistence.repository;

import br.com.setebit.ranking.infrastructure.persistence.entity.TurmaJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTurmaRepository extends JpaRepository<TurmaJpaEntity, Long> {

	List<TurmaJpaEntity> findByAtivoTrueOrderByNomeAsc();
}
