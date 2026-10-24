package br.com.setebit.ranking.infrastructure.persistence.repository;

import br.com.setebit.ranking.infrastructure.persistence.entity.TrimestreJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTrimestreRepository extends JpaRepository<TrimestreJpaEntity, Long> {

	List<TrimestreJpaEntity> findByTurmaIdOrderByCreatedAtDesc(Long turmaId);
}
