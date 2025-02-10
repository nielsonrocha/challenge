package com.github.nielsonrocha.challenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record ContatoDTO(
    @JsonProperty("name") @NotNull String nome, @JsonProperty("main") boolean principal) {}
