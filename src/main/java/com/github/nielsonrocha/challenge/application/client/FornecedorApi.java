package com.github.nielsonrocha.challenge.application.client;

import com.github.nielsonrocha.challenge.domain.dto.PedidoFornecedorRequest;
import com.github.nielsonrocha.challenge.domain.dto.PedidoFornecedorResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "FornecedorApi",
    url = "${app.fornecedor.url}",
    path = "/api/v1/suppliers"
)
public interface FornecedorApi {

  @Retry(name = "fornecedorApiRetry")
  @PostMapping("/order")
  PedidoFornecedorResponse criarPedido(@RequestBody PedidoFornecedorRequest pedido);
}
