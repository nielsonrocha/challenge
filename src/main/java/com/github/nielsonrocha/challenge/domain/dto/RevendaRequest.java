package com.github.nielsonrocha.challenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.nielsonrocha.challenge.domain.validation.Cnpj;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record RevendaRequest(
    @NotBlank @Cnpj @Size(max = 14, min = 14) String cnpj,
    @JsonProperty("companyName") @NotBlank String razaoSocial,
    @JsonProperty("tradeName") @NotBlank String nomeFantasia,
    @NotBlank String email,
    @JsonProperty("phones") List<TelefoneDTO> telefones,
    @JsonProperty("contacts") @NotEmpty List<ContatoDTO> contatos,
    @JsonProperty("addresses") @NotEmpty List<EndrecoDTO> enderecos) {}
