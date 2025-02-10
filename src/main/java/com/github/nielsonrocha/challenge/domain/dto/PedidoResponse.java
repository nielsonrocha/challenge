package com.github.nielsonrocha.challenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.nielsonrocha.challenge.domain.model.ItemPedido;
import com.github.nielsonrocha.challenge.domain.model.PedidoStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponse(
    UUID uuid,
    @JsonProperty("createdAt") LocalDateTime dataCriacao,
    PedidoStatus status,
    @JsonProperty("customer") ClienteDTO cliente,
    @JsonProperty("items") List<ItemPedido> itens) {}
