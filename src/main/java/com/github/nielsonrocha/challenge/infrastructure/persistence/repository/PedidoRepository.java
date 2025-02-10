package com.github.nielsonrocha.challenge.infrastructure.persistence.repository;

import com.github.nielsonrocha.challenge.domain.model.PedidoStatus;
import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.PedidoEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

  @Query(
      "select p from PedidoEntity p join fetch p.revenda r join fetch p.cliente c join fetch p.itens i where r.uuid = :uuid")
  Page<PedidoEntity> findAllByRevendaUuid(UUID uuid, Pageable pageable);

  @Query(
      "select p from PedidoEntity p join fetch p.revenda r join fetch p.cliente c join fetch p.itens i where p.uuid = :uuid")
  Optional<PedidoEntity> findByUuid(UUID uuid);

  @Query(
      "select p from PedidoEntity p join fetch p.revenda r join fetch p.cliente c join fetch p.itens i where r.uuid = :uuid and p.status = :pedidoStatus")
  List<PedidoEntity> findAllByRevendaUuidAndStatus(UUID uuid, PedidoStatus pedidoStatus);

  @Modifying
  @Transactional
  @Query("UPDATE PedidoEntity p SET p.status = :status WHERE p.id IN :ids")
  int atualizarStatus(@Param("status") PedidoStatus status, @Param("ids") List<Long> ids);

}
