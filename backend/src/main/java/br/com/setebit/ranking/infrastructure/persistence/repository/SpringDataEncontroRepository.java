package br.com.setebit.ranking.infrastructure.persistence.repository;

import br.com.setebit.ranking.infrastructure.persistence.entity.EncontroJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataEncontroRepository extends JpaRepository<EncontroJpaEntity, Long> {

	List<EncontroJpaEntity> findByPeriodoIdOrderByNumeroAsc(Long periodoId);

	int countByPeriodoId(Long periodoId);
}
