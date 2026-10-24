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
			where n.encontroId in (select e.id from EncontroJpaEntity e where e.trimestreId = :trimestreId)
			""")
	List<NotaJpaEntity> findByTrimestreId(@Param("trimestreId") Long trimestreId);

	@Query("""
			select n from NotaJpaEntity n
			where n.encontroId in (select e.id from EncontroJpaEntity e where e.trimestreId = :trimestreId)
			  and (:encontroId is null or n.encontroId = :encontroId)
			  and (:criterioId is null or n.criterioId = :criterioId)
			""")
	List<NotaJpaEntity> findByTrimestreIdFiltered(
			@Param("trimestreId") Long trimestreId,
			@Param("encontroId") Long encontroId,
			@Param("criterioId") Long criterioId);

	@Query("""
			select n from NotaJpaEntity n
			where n.alunoId = :alunoId
			  and n.encontroId in (select e.id from EncontroJpaEntity e where e.trimestreId = :trimestreId)
			""")
	List<NotaJpaEntity> findByAlunoIdAndTrimestreId(@Param("alunoId") Long alunoId, @Param("trimestreId") Long trimestreId);

	void deleteByAlunoId(Long alunoId);
}
