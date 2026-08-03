package br.com.setebit.ranking.infrastructure.persistence.repository;

import br.com.setebit.ranking.infrastructure.persistence.entity.PeriodoJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPeriodoRepository extends JpaRepository<PeriodoJpaEntity, Long> {

	List<PeriodoJpaEntity> findByTurmaIdOrderByCreatedAtDesc(Long turmaId);
}
