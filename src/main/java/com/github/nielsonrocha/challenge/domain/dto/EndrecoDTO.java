package com.github.nielsonrocha.challenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record EndrecoDTO(@JsonProperty("street") @NotBlank String logradouro,
                         @JsonProperty("number") @NotBlank String numero,
                         @JsonProperty("complement")String complemento,
                         @JsonProperty("district") @NotBlank String bairro,
                         @JsonProperty("city") @NotBlank String cidade,
                         @JsonProperty("state") @NotBlank String uf,
                         @JsonProperty("postalCode") @NotBlank String cep) {}
