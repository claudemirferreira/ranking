package br.com.setebit.ranking.infrastructure.persistence.repository;

import br.com.setebit.ranking.infrastructure.persistence.entity.SabadoJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSabadoRepository extends JpaRepository<SabadoJpaEntity, Long> {

	List<SabadoJpaEntity> findByTrimestreIdOrderByNumeroAsc(Long trimestreId);

	int countByTrimestreId(Long trimestreId);
}
