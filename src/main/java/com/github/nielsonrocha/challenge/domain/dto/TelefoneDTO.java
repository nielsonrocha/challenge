package com.github.nielsonrocha.challenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record TelefoneDTO(
    @JsonProperty("areaCode") @NotBlank String ddd,
    @JsonProperty("number") @NotBlank String numero) {}
