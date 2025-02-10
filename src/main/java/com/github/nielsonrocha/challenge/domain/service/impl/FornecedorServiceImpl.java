package com.github.nielsonrocha.challenge.domain.service.impl;

import com.github.nielsonrocha.challenge.application.client.FornecedorApi;
import com.github.nielsonrocha.challenge.application.exception.IntegrationException;
import com.github.nielsonrocha.challenge.application.exception.NotFoundException;
import com.github.nielsonrocha.challenge.domain.dto.PedidoFornecedorRequest;
import com.github.nielsonrocha.challenge.domain.dto.PedidoFornecedorResponse;
import com.github.nielsonrocha.challenge.domain.model.FornecedorPedidoStatus;
import com.github.nielsonrocha.challenge.domain.model.ItemPedido;
import com.github.nielsonrocha.challenge.domain.model.Pedido;
import com.github.nielsonrocha.challenge.domain.model.PedidoStatus;
import com.github.nielsonrocha.challenge.domain.model.Revenda;
import com.github.nielsonrocha.challenge.domain.service.FornecedorService;
import com.github.nielsonrocha.challenge.domain.service.PedidoService;
import com.github.nielsonrocha.challenge.domain.service.RevendaService;
import com.github.nielsonrocha.challenge.infrastructure.mapper.PedidoMapper;
import com.github.nielsonrocha.challenge.infrastructure.mapper.RevendaMapper;
import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.FornecedorPedidoEntity;
import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.FornecedorPedidoItemEntity;
import com.github.nielsonrocha.challenge.infrastructure.persistence.repository.FornecedorPedidoItemRepository;
import com.github.nielsonrocha.challenge.infrastructure.persistence.repository.FornecedorPedidoRepository;
import feign.FeignException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FornecedorServiceImpl implements FornecedorService {

  private final FornecedorApi fornecedorApi;
  private final PedidoService pedidoService;
  private final RevendaService revendaService;
  private final FornecedorPedidoRepository fornecedorPedidoRepository;
  private final FornecedorPedidoItemRepository fornecedorPedidoItemRepository;
  private final RevendaMapper revendaMapper;
  private final PedidoMapper pedidoMapper;
  private static final int PEDIDO_MINIMO = 1000;

  @Transactional
  public PedidoFornecedorResponse enviarPedidoFornecedor(UUID revendaUuid) {
    log.info("Criando pedido para fornecedor");

    var revenda =
        revendaService
            .buscarPorUuid(revendaUuid)
            .orElseThrow(() -> new NotFoundException("Revenda não encontrada"));

    var pedidosPendentes = pedidoService.buscarPedidosPendentesPorRevenda(revenda.uuid());
    if (pedidosPendentes.isEmpty()) {
      return new PedidoFornecedorResponse("", "Error", "Nenhum pedido pendente para envio");
    }

    var totalItens =
        pedidosPendentes.stream()
            .flatMap(pedido -> pedido.itens().stream())
            .mapToLong(ItemPedido::quantidade)
            .sum();

    if (totalItens < PEDIDO_MINIMO) {
      return new PedidoFornecedorResponse("", "Error", "Quantidade mínima não atingida");
    }

    var fornecedorPedido = criarPedidoFornecedor(revenda, pedidosPendentes);
    pedidoService.alterarStatusPedidos(pedidosPendentes, PedidoStatus.AGUARDANDO_FORNECEDOR);

    var pedidoFornecedorRequest =
        new PedidoFornecedorRequest(
            revenda.uuid(),
            pedidosPendentes.stream().flatMap(pedido -> pedido.itens().stream()).toList());

    return enviarPedidoParaFornecedor(fornecedorPedido, pedidoFornecedorRequest);
  }

  private PedidoFornecedorResponse enviarPedidoParaFornecedor(
      FornecedorPedidoEntity fornecedorPedido, PedidoFornecedorRequest pedidoFornecedorRequest) {

    try {
      var response = fornecedorApi.criarPedido(pedidoFornecedorRequest);
      log.info(
          "Pedido enviado para fornecedor: {} Status: {}", response.message(), response.status());

      fornecedorPedido.setIdExterno(response.orderId());
      fornecedorPedido.setStatus(FornecedorPedidoStatus.EM_PROCESSAMENTO);
      fornecedorPedidoRepository.save(fornecedorPedido);

      return response;
    } catch (FeignException.NotFound e) {
      throw new NotFoundException("Fornecedor não encontrado para o pedido");
    } catch (FeignException.BadRequest e) {
      throw new IntegrationException("Erro ao enviar pedido para fornecedor: " + e.contentUTF8());
    } catch (FeignException e) {
      throw new IntegrationException("Erro ao comunicar com fornecedor: " + e.status());
    } catch (Exception e) {
      throw new IntegrationException("Erro inesperado ao processar pedido");
    }
  }

  private FornecedorPedidoEntity criarPedidoFornecedor(Revenda revenda, List<Pedido> pedidos) {
    var fornecedorPedido =
        fornecedorPedidoRepository.save(
            FornecedorPedidoEntity.builder()
                .status(FornecedorPedidoStatus.EM_PROCESSAMENTO)
                .revenda(revendaMapper.toEntity(revenda))
                .build());

    fornecedorPedidoItemRepository.saveAll(
        pedidos.stream()
            .flatMap(
                pedido ->
                    pedido.itens().stream()
                        .map(
                            item ->
                                FornecedorPedidoItemEntity.builder()
                                    .fornecedorPedido(fornecedorPedido)
                                    .pedido(pedidoMapper.toEntity(pedido))
                                    .produto(item.produto())
                                    .quantidade(item.quantidade())
                                    .build()))
            .toList());

    return fornecedorPedido;
  }
}
