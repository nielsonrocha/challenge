package com.github.nielsonrocha.challenge.infrastructure.persistence.repository;

import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.RevendaEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RevendaRepository extends JpaRepository<RevendaEntity, Long> {

  Optional<RevendaEntity> findByUuid(UUID uuid);

  @Query("select r.id from RevendaEntity r")
  Page<UUID> findAllRevendaIds(Pageable pageable);

  @Query(
      "select r from RevendaEntity r "
          + "join fetch r.telefones "
          + "join fetch r.contatos "
          + "join fetch r.enderecos "
          + "where r.id in :ids")
  List<RevendaEntity> findAllByIds(@Param("ids") List<UUID> ids);
}
