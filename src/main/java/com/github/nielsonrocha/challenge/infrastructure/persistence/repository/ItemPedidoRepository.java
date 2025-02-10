package com.github.nielsonrocha.challenge.infrastructure.persistence.repository;

import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.ItemPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedidoEntity, Long> {}
