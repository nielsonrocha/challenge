package com.github.nielsonrocha.challenge.domain.model;

import java.util.UUID;

public record Cliente(UUID uuid, String nome, String cpf, String email) {}
