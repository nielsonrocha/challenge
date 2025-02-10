package com.github.nielsonrocha.challenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.nielsonrocha.challenge.domain.model.ItemPedido;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoRequest(
    @JsonProperty("customer") @NotNull ClienteDTO cliente,
    @JsonProperty("items") @NotEmpty List<ItemPedido> itens) {}
