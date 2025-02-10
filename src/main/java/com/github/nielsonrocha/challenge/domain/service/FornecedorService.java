package com.github.nielsonrocha.challenge.domain.service;

import com.github.nielsonrocha.challenge.domain.dto.PedidoFornecedorResponse;
import java.util.UUID;

public interface FornecedorService {

  PedidoFornecedorResponse enviarPedidoFornecedor(UUID revendaUuid);
}
