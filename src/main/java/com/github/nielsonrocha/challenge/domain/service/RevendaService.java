package com.github.nielsonrocha.challenge.domain.service;

import com.github.nielsonrocha.challenge.domain.dto.PageDTO;
import com.github.nielsonrocha.challenge.domain.dto.RevendaResponse;
import com.github.nielsonrocha.challenge.domain.model.Revenda;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface RevendaService {

  Revenda criarRevenda(Revenda revenda);

  Revenda atualizarRevenda(String uuid, Revenda revenda);

  PageDTO<RevendaResponse> buscarTodos(Pageable pageable);

  Optional<Revenda> buscarPorUuid(UUID uuid);
}
