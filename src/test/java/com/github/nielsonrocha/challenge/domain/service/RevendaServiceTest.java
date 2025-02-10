package com.github.nielsonrocha.challenge.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.nielsonrocha.challenge.application.exception.BusinessException;
import com.github.nielsonrocha.challenge.application.exception.NotFoundException;
import com.github.nielsonrocha.challenge.domain.dto.PageDTO;
import com.github.nielsonrocha.challenge.domain.dto.RevendaResponse;
import com.github.nielsonrocha.challenge.domain.model.Contato;
import com.github.nielsonrocha.challenge.domain.model.Revenda;
import com.github.nielsonrocha.challenge.domain.service.impl.RevendaServiceImpl;
import com.github.nielsonrocha.challenge.infrastructure.mapper.RevendaMapper;
import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.RevendaEntity;
import com.github.nielsonrocha.challenge.infrastructure.persistence.repository.RevendaRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class RevendaServiceTest {

  @Mock private RevendaRepository revendaRepository;

  @Mock private RevendaMapper revendaMapper;

  @InjectMocks private RevendaServiceImpl revendaService;

  private UUID uuid;
  private Revenda revenda;
  private RevendaEntity revendaEntity;

  @BeforeEach
  void setUp() {
    uuid = UUID.randomUUID();
    revendaEntity = new RevendaEntity();
    revendaEntity.setUuid(uuid);
    revenda =
        new Revenda(
            1L,
            uuid,
            "12345678000195",
            "Razão Social",
            "Nome Fantasia",
            "email@example.com",
            Set.of(),
            Set.of(),
            Set.of());
  }

  @Test
  void deveCriarRevenda() {
    when(revendaMapper.toEntity(revenda)).thenReturn(revendaEntity);
    when(revendaRepository.save(revendaEntity)).thenReturn(revendaEntity);
    when(revendaMapper.toModel(revendaEntity)).thenReturn(revenda);

    Revenda resultado = revendaService.criarRevenda(revenda);

    assertNotNull(resultado);
    verify(revendaRepository).save(revendaEntity);
  }

  @Test
  void deveLancarExcecaoSeMaisDeUmContatoPrincipal() {
    Contato contato1 = new Contato(UUID.randomUUID(), "Joao", true);
    Contato contato2 = new Contato(UUID.randomUUID(), "Jose", true);

    Set<Contato> contatos = Set.of(contato1, contato2);

    assertThrows(
        BusinessException.class,
        () ->
            new Revenda(
                1L,
                uuid,
                "12345678000195",
                "Razão Social",
                "Nome Fantasia",
                "email@example.com",
                Set.of(),
                Set.of(),
                contatos));
  }

  @Test
  void deveAtualizarRevenda() {
    when(revendaRepository.findByUuid(uuid)).thenReturn(Optional.of(revendaEntity));
    when(revendaMapper.toEntity(revenda)).thenReturn(revendaEntity);
    when(revendaRepository.save(revendaEntity)).thenReturn(revendaEntity);
    when(revendaMapper.toModel(revendaEntity)).thenReturn(revenda);

    Revenda resultado = revendaService.atualizarRevenda(uuid.toString(), revenda);

    assertNotNull(resultado);
    verify(revendaRepository).save(revendaEntity);
  }

  @Test
  void deveLancarNotFoundExceptionAoAtualizarRevendaNaoExistente() {
    when(revendaRepository.findByUuid(uuid)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> revendaService.atualizarRevenda(uuid.toString(), revenda));
  }

  @Test
  void deveBuscarTodos() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<UUID> idsPage = new PageImpl<>(List.of(uuid), pageable, 1);
    List<RevendaEntity> revendas = List.of(revendaEntity);
    List<RevendaResponse> responseList =
        List.of(
            new RevendaResponse(
                uuid,
                "12345678000195",
                "Razão Social",
                "Nome Fantasia",
                "email@example.com",
                Set.of(),
                Set.of(),
                Set.of()));

    when(revendaRepository.findAllRevendaIds(pageable)).thenReturn(idsPage);
    when(revendaRepository.findAllByIds(idsPage.getContent())).thenReturn(revendas);


    PageDTO<RevendaResponse> resultado = revendaService.buscarTodos(pageable);

    assertNotNull(resultado);
    assertEquals(1, resultado.content().size());
  }

  @Test
  void deveBuscarPorUuid() {
    when(revendaRepository.findByUuid(uuid)).thenReturn(Optional.of(revendaEntity));
    when(revendaMapper.toModel(revendaEntity)).thenReturn(revenda);

    Optional<Revenda> resultado = revendaService.buscarPorUuid(uuid);

    assertTrue(resultado.isPresent());
  }

  @Test
  void deveRetornarVazioAoBuscarPorUuidNaoExistente() {
    when(revendaRepository.findByUuid(uuid)).thenReturn(Optional.empty());

    Optional<Revenda> resultado = revendaService.buscarPorUuid(uuid);

    assertFalse(resultado.isPresent());
  }
}
