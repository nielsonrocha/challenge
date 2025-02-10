package com.github.nielsonrocha.challenge.domain.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemPedido(
    @JsonProperty("product") String produto,
    @JsonProperty("quantity") Long quantidade
) {}
