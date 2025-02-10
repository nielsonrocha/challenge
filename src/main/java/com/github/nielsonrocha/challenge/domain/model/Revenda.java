package com.github.nielsonrocha.challenge.domain.model;

import com.github.nielsonrocha.challenge.application.exception.BusinessException;
import java.util.Set;
import java.util.UUID;

public record Revenda(
    Long id,
    UUID uuid,
    String cnpj,
    String razaoSocial,
    String nomeFantasia,
    String email,
    Set<Telefone> telefones,
    Set<Endereco> enderecos,
    Set<Contato> contatos) {

  public Revenda {
    long principalCount = contatos.stream().filter(Contato::principal).count();
    if (principalCount > 1) {
      throw new BusinessException("Only one contact can be marked as principal.");
    }
  }
}
