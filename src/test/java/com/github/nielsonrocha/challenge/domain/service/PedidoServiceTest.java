package com.github.nielsonrocha.challenge.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.nielsonrocha.challenge.application.exception.BusinessException;
import com.github.nielsonrocha.challenge.application.exception.NotFoundException;
import com.github.nielsonrocha.challenge.domain.model.Cliente;
import com.github.nielsonrocha.challenge.domain.model.Pedido;
import com.github.nielsonrocha.challenge.domain.model.PedidoStatus;
import com.github.nielsonrocha.challenge.domain.model.Revenda;
import com.github.nielsonrocha.challenge.domain.service.impl.PedidoServiceImpl;
import com.github.nielsonrocha.challenge.infrastructure.mapper.PedidoMapper;
import com.github.nielsonrocha.challenge.infrastructure.mapper.RevendaMapper;
import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.PedidoEntity;
import com.github.nielsonrocha.challenge.infrastructure.persistence.repository.ClienteRepository;
import com.github.nielsonrocha.challenge.infrastructure.persistence.repository.PedidoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
class PedidoServiceTest {

  @Mock private RevendaService revendaService;
  @Mock private PedidoRepository pedidoRepository;
  @Mock private ClienteRepository clienteRepository;
  @Mock private PedidoMapper pedidoMapper;
  @Mock private RevendaMapper revendaMapper;

  @InjectMocks private PedidoServiceImpl pedidoService;

  private UUID revendaUuid;
  private UUID pedidoUuid;
  private Pedido pedido;
  private PedidoEntity pedidoEntity;

  @BeforeEach
  void setUp() {
    revendaUuid = UUID.randomUUID();
    pedidoUuid = UUID.randomUUID();

    pedidoEntity = new PedidoEntity();
    pedidoEntity.setStatus(PedidoStatus.PENDENTE);

    pedido =
        new Pedido(
            1L,
            pedidoUuid,
            PedidoStatus.PENDENTE,
            new Cliente(UUID.randomUUID(), "Cliente", "000000000", ""),
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of());
  }

  @Test
  void deveCriarPedidoComSucesso() {
    when(revendaService.buscarPorUuid(revendaUuid)).thenReturn(Optional.of(mock(Revenda.class)));
    when(clienteRepository.findByUuid(any()))
        .thenReturn(
            Optional.of(
                mock(
                    com.github.nielsonrocha.challenge.infrastructure.persistence.entity
                        .ClienteEntity.class)));
    when(pedidoMapper.toEntity(pedido)).thenReturn(pedidoEntity);
    when(pedidoRepository.save(pedidoEntity)).thenReturn(pedidoEntity);
    when(pedidoMapper.fromEntity(pedidoEntity)).thenReturn(pedido);

    Pedido resultado = pedidoService.criarPedido(revendaUuid, pedido);

    assertNotNull(resultado);
    verify(pedidoRepository).save(pedidoEntity);
  }

  @Test
  void deveLancarNotFoundExceptionSeRevendaNaoExistirAoCriarPedido() {
    when(revendaService.buscarPorUuid(revendaUuid)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> pedidoService.criarPedido(revendaUuid, pedido));
  }

  @Test
  void deveLancarNotFoundExceptionSeClienteNaoExistirAoCriarPedido() {
    when(revendaService.buscarPorUuid(revendaUuid)).thenReturn(Optional.of(mock(Revenda.class)));
    when(clienteRepository.findByUuid(any())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> pedidoService.criarPedido(revendaUuid, pedido));
  }

  @Test
  void deveBuscarPedidosPorRevenda() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<PedidoEntity> pedidoEntities = new PageImpl<>(List.of(pedidoEntity));

    when(pedidoRepository.findAllByRevendaUuid(revendaUuid, pageable)).thenReturn(pedidoEntities);
    when(pedidoMapper.fromEntity(pedidoEntity)).thenReturn(pedido);

    Page<Pedido> resultado = pedidoService.buscarPedidosPorRevenda(revendaUuid, pageable);

    assertEquals(1, resultado.getContent().size());
  }

  @Test
  void deveBuscarPedidosPendentesPorRevenda() {
    when(pedidoRepository.findAllByRevendaUuidAndStatus(revendaUuid, PedidoStatus.PENDENTE))
        .thenReturn(List.of(pedidoEntity));
    when(pedidoMapper.fromEntity(pedidoEntity)).thenReturn(pedido);

    List<Pedido> resultado = pedidoService.buscarPedidosPendentesPorRevenda(revendaUuid);

    assertEquals(1, resultado.size());
  }

  @Test
  void deveCancelarPedidoComSucesso() {
    when(pedidoRepository.findByUuid(pedidoUuid)).thenReturn(Optional.of(pedidoEntity));

    pedidoService.cancelarPedido(pedidoUuid);

    assertEquals(PedidoStatus.CANCELADO, pedidoEntity.getStatus());
    verify(pedidoRepository).save(pedidoEntity);
  }

  @Test
  void deveLancarNotFoundExceptionAoCancelarPedidoInexistente() {
    when(pedidoRepository.findByUuid(pedidoUuid)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> pedidoService.cancelarPedido(pedidoUuid));
  }

  @Test
  void deveLancarBusinessExceptionAoCancelarPedidoJaFinalizado() {
    pedidoEntity.setStatus(PedidoStatus.FINALIZADO);
    when(pedidoRepository.findByUuid(pedidoUuid)).thenReturn(Optional.of(pedidoEntity));

    assertThrows(BusinessException.class, () -> pedidoService.cancelarPedido(pedidoUuid));
  }
}
