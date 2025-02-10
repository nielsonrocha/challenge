package com.github.nielsonrocha.challenge.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record Pedido(
    Long id,
    UUID uuid,
    PedidoStatus status,
    Cliente cliente,
    LocalDateTime dataCriacao,
    LocalDateTime dataAtualizacao,
    List<ItemPedido> itens
) {}
