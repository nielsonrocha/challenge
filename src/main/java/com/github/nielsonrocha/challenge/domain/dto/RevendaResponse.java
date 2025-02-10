package com.github.nielsonrocha.challenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.UUID;

public record RevendaResponse(
    UUID uuid,
    String cnpj,
    @JsonProperty("companyName") @NotBlank String razaoSocial,
    @JsonProperty("tradeName") @NotBlank String nomeFantasia,
    @NotBlank String email,
    @JsonProperty("phones") Set<TelefoneDTO> telefones,
    @JsonProperty("contacts") @NotEmpty Set<ContatoDTO> contatos,
    @JsonProperty("addresses") @NotEmpty Set<EndrecoDTO> enderecos) {}
