package br.com.setebit.ranking.infrastructure.persistence.repository;

import br.com.setebit.ranking.infrastructure.persistence.entity.NotaJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataNotaRepository extends JpaRepository<NotaJpaEntity, Long> {

	Optional<NotaJpaEntity> findByAlunoIdAndEncontroIdAndCriterioId(Long alunoId, Long encontroId, Long criterioId);

	@Query("""
			select n from NotaJpaEntity n
			where n.encontroId in (select e.id from EncontroJpaEntity e where e.periodoId = :periodoId)
			""")
	List<NotaJpaEntity> findByPeriodoId(@Param("periodoId") Long periodoId);

	@Query("""
			select n from NotaJpaEntity n
			where n.encontroId in (select e.id from EncontroJpaEntity e where e.periodoId = :periodoId)
			  and (:encontroId is null or n.encontroId = :encontroId)
			  and (:criterioId is null or n.criterioId = :criterioId)
			""")
	List<NotaJpaEntity> findByPeriodoIdFiltered(
			@Param("periodoId") Long periodoId,
			@Param("encontroId") Long encontroId,
			@Param("criterioId") Long criterioId);

	@Query("""
			select n from NotaJpaEntity n
			where n.alunoId = :alunoId
			  and n.encontroId in (select e.id from EncontroJpaEntity e where e.periodoId = :periodoId)
			""")
	List<NotaJpaEntity> findByAlunoIdAndPeriodoId(@Param("alunoId") Long alunoId, @Param("periodoId") Long periodoId);
}
