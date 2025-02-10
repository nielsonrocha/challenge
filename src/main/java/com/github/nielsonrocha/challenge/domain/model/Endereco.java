package com.github.nielsonrocha.challenge.domain.model;

import java.util.UUID;

public record Endereco(
    UUID uuid,
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    String cidade,
    String uf,
    String cep) {}
