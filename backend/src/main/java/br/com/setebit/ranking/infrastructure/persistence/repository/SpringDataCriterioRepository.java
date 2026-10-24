package br.com.setebit.ranking.infrastructure.persistence.repository;

import br.com.setebit.ranking.infrastructure.persistence.entity.CriterioJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCriterioRepository extends JpaRepository<CriterioJpaEntity, Long> {

	List<CriterioJpaEntity> findByTrimestreIdOrderByOrdemAsc(Long trimestreId);
}
