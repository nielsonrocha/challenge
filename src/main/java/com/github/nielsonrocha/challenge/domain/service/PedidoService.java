package com.github.nielsonrocha.challenge.domain.service;

import com.github.nielsonrocha.challenge.domain.model.Pedido;
import com.github.nielsonrocha.challenge.domain.model.PedidoStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoService {

  Pedido criarPedido(UUID revendaUuid, Pedido pedido);

  Page<Pedido> buscarPedidosPorRevenda(UUID revendaUuid, Pageable pageable);
  
  Optional<Pedido> buscarPorUuid(UUID uuid);

  List<Pedido> buscarPedidosPendentesPorRevenda(UUID revendaUuid);

  void cancelarPedido(UUID uuid);

  void alterarStatusPedidos(List<Pedido> pedidos, PedidoStatus status);
}
