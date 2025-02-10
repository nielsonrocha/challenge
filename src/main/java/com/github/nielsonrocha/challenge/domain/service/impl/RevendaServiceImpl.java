package com.github.nielsonrocha.challenge.domain.service.impl;

import com.github.nielsonrocha.challenge.application.exception.NotFoundException;
import com.github.nielsonrocha.challenge.domain.dto.PageDTO;
import com.github.nielsonrocha.challenge.domain.dto.RevendaResponse;
import com.github.nielsonrocha.challenge.domain.model.Revenda;
import com.github.nielsonrocha.challenge.domain.service.RevendaService;
import com.github.nielsonrocha.challenge.infrastructure.mapper.RevendaMapper;
import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.RevendaEntity;
import com.github.nielsonrocha.challenge.infrastructure.persistence.repository.RevendaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RevendaServiceImpl implements RevendaService {

  private final RevendaRepository revendaRepository;
  private final RevendaMapper revendaMapper;

  @Override
  public Revenda criarRevenda(Revenda revenda) {
    var revendaEntity = revendaMapper.toEntity(revenda);
    var saved = revendaRepository.save(revendaEntity);
    return revendaMapper.toModel(saved);
  }

  @Override
  public Revenda atualizarRevenda(String uuid, Revenda revenda) {
    var revendaEntity =
        revendaRepository
            .findByUuid(UUID.fromString(uuid))
            .orElseThrow(() -> new NotFoundException("Revenda não encontrada"));

    var revendaEntityUpdate = revendaMapper.toEntity(revenda);
    revendaEntityUpdate.setId(revendaEntity.getId());
    revendaEntityUpdate.setUuid(revendaEntity.getUuid());

    var saved = revendaRepository.save(revendaEntityUpdate);
    return revendaMapper.toModel(saved);
  }

  @Override
  public PageDTO<RevendaResponse> buscarTodos(Pageable pageable) {
    Page<UUID> idsPage = revendaRepository.findAllRevendaIds(pageable);
    List<RevendaEntity> revendas = revendaRepository.findAllByIds(idsPage.getContent());

    return PageDTO.from(
        revendas.stream().map(revendaMapper::toModel).map(revendaMapper::toResponse).toList(),
        pageable,
        idsPage.getTotalElements());
  }

  @Override
  public Optional<Revenda> buscarPorUuid(UUID uuid) {
    return revendaRepository.findByUuid(uuid).map(revendaMapper::toModel);
  }

}
