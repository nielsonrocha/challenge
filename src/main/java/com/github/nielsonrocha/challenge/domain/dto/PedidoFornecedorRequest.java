package com.github.nielsonrocha.challenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.nielsonrocha.challenge.domain.model.ItemPedido;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record PedidoFornecedorRequest(
    @JsonProperty("resaleId") @NotNull UUID revendaId,
    @JsonProperty("items") @NotEmpty List<ItemPedido> itens) {}
