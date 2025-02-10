package com.github.nielsonrocha.challenge.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.nielsonrocha.challenge.application.client.FornecedorApi;
import com.github.nielsonrocha.challenge.application.exception.NotFoundException;
import com.github.nielsonrocha.challenge.domain.dto.PedidoFornecedorResponse;
import com.github.nielsonrocha.challenge.domain.model.ItemPedido;
import com.github.nielsonrocha.challenge.domain.model.Pedido;
import com.github.nielsonrocha.challenge.domain.model.Revenda;
import com.github.nielsonrocha.challenge.domain.service.impl.FornecedorServiceImpl;
import com.github.nielsonrocha.challenge.infrastructure.mapper.PedidoMapper;
import com.github.nielsonrocha.challenge.infrastructure.mapper.RevendaMapper;
import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.FornecedorPedidoEntity;
import com.github.nielsonrocha.challenge.infrastructure.persistence.repository.FornecedorPedidoItemRepository;
import com.github.nielsonrocha.challenge.infrastructure.persistence.repository.FornecedorPedidoRepository;
import feign.FeignException;
import java.time.LocalDateTime;
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

@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

  @Mock private FornecedorApi fornecedorApi;
  @Mock private PedidoService pedidoService;
  @Mock private RevendaService revendaService;
  @Mock private FornecedorPedidoRepository fornecedorPedidoRepository;
  @Mock private FornecedorPedidoItemRepository fornecedorPedidoItemRepository;
  @Mock private RevendaMapper revendaMapper;
  @Mock private PedidoMapper pedidoMapper;

  @InjectMocks private FornecedorServiceImpl fornecedorService;

  private UUID revendaUuid;
  private Revenda revenda;

  @BeforeEach
  void setup() {
    revendaUuid = UUID.randomUUID();
    revenda =
        new Revenda(
            1L,
            revendaUuid,
            "12345678000199",
            "Revenda X",
            "Fantasia X",
            "revenda@email.com",
            Set.of(),
            Set.of(),
            Set.of());
  }

  @Test
  void enviarPedidoFornecedor_RevendaNaoEncontrada() {
    when(revendaService.buscarPorUuid(revendaUuid)).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> fornecedorService.enviarPedidoFornecedor(revendaUuid));
  }

  @Test
  void enviarPedidoFornecedor_NenhumPedidoPendente() {
    when(revendaService.buscarPorUuid(revendaUuid)).thenReturn(Optional.of(revenda));
    when(pedidoService.buscarPedidosPendentesPorRevenda(revendaUuid)).thenReturn(List.of());

    PedidoFornecedorResponse response = fornecedorService.enviarPedidoFornecedor(revendaUuid);
    assertEquals("Error", response.status());
    assertEquals("Nenhum pedido pendente para envio", response.message());
  }

  @Test
  void enviarPedidoFornecedor_QuantidadeMinimaNaoAtingida() {
    ItemPedido item = new ItemPedido(null, 500L);
    Pedido pedido =
        new Pedido(null, null, null, null, LocalDateTime.now(), LocalDateTime.now(), List.of(item));

    when(revendaService.buscarPorUuid(revendaUuid)).thenReturn(Optional.of(revenda));
    when(pedidoService.buscarPedidosPendentesPorRevenda(revendaUuid)).thenReturn(List.of(pedido));

    PedidoFornecedorResponse response = fornecedorService.enviarPedidoFornecedor(revendaUuid);
    assertEquals("Error", response.status());
    assertEquals("Quantidade mínima não atingida", response.message());
  }

  @Test
  void enviarPedidoFornecedor_Sucesso() {
    ItemPedido item = new ItemPedido(null, 2000L);
    Pedido pedido =
        new Pedido(null, null, null, null, LocalDateTime.now(), LocalDateTime.now(), List.of(item));
    FornecedorPedidoEntity fornecedorPedidoEntity = new FornecedorPedidoEntity();

    when(revendaService.buscarPorUuid(revendaUuid)).thenReturn(Optional.of(revenda));
    when(pedidoService.buscarPedidosPendentesPorRevenda(revendaUuid)).thenReturn(List.of(pedido));
    when(fornecedorPedidoRepository.save(any())).thenReturn(fornecedorPedidoEntity);
    when(fornecedorApi.criarPedido(any()))
        .thenReturn(new PedidoFornecedorResponse("12345", "Success", "Pedido criado"));

    PedidoFornecedorResponse response = fornecedorService.enviarPedidoFornecedor(revendaUuid);
    assertEquals("Success", response.status());
    assertEquals("Pedido criado", response.message());
  }

  @Test
  void enviarPedidoFornecedor_ErroFornecedorNotFound() {
    when(revendaService.buscarPorUuid(revendaUuid)).thenReturn(Optional.of(revenda));
    when(pedidoService.buscarPedidosPendentesPorRevenda(revendaUuid))
        .thenReturn(
            List.of(
                new Pedido(
                    null,
                    null,
                    null,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    List.of(new ItemPedido(null, 2000L)))));
    when(fornecedorPedidoRepository.save(any())).thenReturn(new FornecedorPedidoEntity());
    when(fornecedorApi.criarPedido(any())).thenThrow(FeignException.NotFound.class);

    assertThrows(
        NotFoundException.class, () -> fornecedorService.enviarPedidoFornecedor(revendaUuid));
  }
}
