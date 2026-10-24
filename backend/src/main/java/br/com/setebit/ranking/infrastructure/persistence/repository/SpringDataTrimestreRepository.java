package br.com.setebit.ranking.infrastructure.persistence.repository;

import br.com.setebit.ranking.infrastructure.persistence.entity.TrimestreJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTrimestreRepository extends JpaRepository<TrimestreJpaEntity, Long> {

	Optional<TrimestreJpaEntity> findByTurmaIdAndAnoAndNomeIgnoreCase(Long turmaId, int ano, String nome);

	List<TrimestreJpaEntity> findByTurmaIdOrderByAnoDescNomeAsc(Long turmaId);
}
