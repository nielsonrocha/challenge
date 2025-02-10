package com.github.nielsonrocha.challenge.domain.service.impl;

import com.github.nielsonrocha.challenge.application.exception.BusinessException;
import com.github.nielsonrocha.challenge.application.exception.NotFoundException;
import com.github.nielsonrocha.challenge.domain.model.Pedido;
import com.github.nielsonrocha.challenge.domain.model.PedidoStatus;
import com.github.nielsonrocha.challenge.domain.service.PedidoService;
import com.github.nielsonrocha.challenge.domain.service.RevendaService;
import com.github.nielsonrocha.challenge.infrastructure.mapper.PedidoMapper;
import com.github.nielsonrocha.challenge.infrastructure.mapper.RevendaMapper;
import com.github.nielsonrocha.challenge.infrastructure.persistence.repository.ClienteRepository;
import com.github.nielsonrocha.challenge.infrastructure.persistence.repository.PedidoRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoServiceImpl implements PedidoService {

  private final RevendaService revendaService;
  private final PedidoRepository pedidoRepository;
  private final ClienteRepository clienteRepository;
  private final PedidoMapper pedidoMapper;
  private final RevendaMapper revendaMapper;

  @Transactional
  public Pedido criarPedido(UUID revendaUuid, Pedido pedido) {
    var revenda = revendaService.buscarPorUuid(revendaUuid);
    if (revenda.isPresent()) {
      var cliente = clienteRepository.findByUuid(pedido.cliente().uuid());
      if (cliente.isPresent()) {
        var pedidoEntity = pedidoMapper.toEntity(pedido);
        pedidoEntity.setStatus(PedidoStatus.PENDENTE);
        pedidoEntity.setCliente(cliente.get());
        pedidoEntity.setRevenda(revendaMapper.toEntity(revenda.get()));
        return pedidoMapper.fromEntity(pedidoRepository.save(pedidoEntity));
      } else {
        throw new NotFoundException("Cliente não encontrado");
      }
    } else {
      throw new NotFoundException("Revenda não encontrada");
    }
  }

  public Page<Pedido> buscarPedidosPorRevenda(UUID revendaUuid, Pageable pageable) {
    return pedidoRepository
        .findAllByRevendaUuid(revendaUuid, pageable)
        .map(pedidoMapper::fromEntity);
  }

  public List<Pedido> buscarPedidosPendentesPorRevenda(UUID revendaUuid) {
    return pedidoRepository
        .findAllByRevendaUuidAndStatus(revendaUuid, PedidoStatus.PENDENTE)
        .stream()
        .map(pedidoMapper::fromEntity)
        .toList();
  }

  public Optional<Pedido> buscarPorUuid(UUID uuid) {
    return pedidoRepository.findByUuid(uuid).map(pedidoMapper::fromEntity);
  }

  @Transactional
  public void cancelarPedido(UUID uuid) {
    var pedido =
        pedidoRepository
            .findByUuid(uuid)
            .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));
    if (!pedido.getStatus().equals(PedidoStatus.PENDENTE)) {
      throw new BusinessException("Somente pedidos pendentes podem ser cancelados");
    }
    pedido.setStatus(PedidoStatus.CANCELADO);
    pedidoRepository.save(pedido);
  }

  @Override
  public void alterarStatusPedidos(List<Pedido> pedidos, PedidoStatus status) {
    var ids = pedidos.stream().map(Pedido::id).collect(Collectors.toList());
    var atualizados = pedidoRepository.atualizarStatus(status, ids);
    log.info("Total de pedidos atualizados: {}", atualizados);
  }
}
