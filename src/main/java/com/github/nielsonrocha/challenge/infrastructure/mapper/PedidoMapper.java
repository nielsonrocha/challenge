package com.github.nielsonrocha.challenge.infrastructure.mapper;

import com.github.nielsonrocha.challenge.domain.dto.PedidoRequest;
import com.github.nielsonrocha.challenge.domain.dto.PedidoResponse;
import com.github.nielsonrocha.challenge.domain.model.Pedido;
import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.PedidoEntity;
import java.util.Objects;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PedidoMapper {

  PedidoEntity toEntity(Pedido pedido);

  Pedido fromEntity(PedidoEntity order);

  Pedido fromRequest(PedidoRequest request);

  PedidoResponse toResponse(Pedido pedido);

  @AfterMapping
  default void setRelationship(@MappingTarget PedidoEntity pedidoEntity) {
    if (Objects.nonNull(pedidoEntity.getItens())) {
      pedidoEntity.getItens().forEach(pedido -> pedido.setPedido(pedidoEntity));
    }
  }
}
