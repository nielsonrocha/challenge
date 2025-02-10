package com.github.nielsonrocha.challenge.infrastructure.mapper;

import com.github.nielsonrocha.challenge.domain.dto.RevendaRequest;
import com.github.nielsonrocha.challenge.domain.dto.RevendaResponse;
import com.github.nielsonrocha.challenge.domain.model.Revenda;
import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.RevendaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RevendaMapper {

  Revenda toModel(RevendaRequest request);

  RevendaEntity toEntity(Revenda model);

  Revenda toModel(RevendaEntity entity);

  RevendaResponse toResponse(Revenda model);

  @AfterMapping
  default void setRelationships(@MappingTarget RevendaEntity revendaEntity) {
    if (revendaEntity.getTelefones() != null) {
      revendaEntity
          .getTelefones()
          .forEach(telefoneEntity -> telefoneEntity.setRevenda(revendaEntity));
    }
    if (revendaEntity.getEnderecos() != null) {
      revendaEntity
          .getEnderecos()
          .forEach(enderecoEntity -> enderecoEntity.setRevenda(revendaEntity));
    }
    if (revendaEntity.getContatos() != null) {
      revendaEntity.getContatos().forEach(contatoEntity -> contatoEntity.setRevenda(revendaEntity));
    }
  }
}
