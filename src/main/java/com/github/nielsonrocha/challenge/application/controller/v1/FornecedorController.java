package com.github.nielsonrocha.challenge.application.controller.v1;

import com.github.nielsonrocha.challenge.domain.dto.PedidoFornecedorResponse;
import com.github.nielsonrocha.challenge.domain.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Supplier", description = "Operations related to Suppliers")
@RestController
@RequestMapping("/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
public class FornecedorController {

  private final FornecedorService fornecedorService;

  @Operation(summary = "Create a new Order for a Supplier")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Order created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request"),
    @ApiResponse(responseCode = "422", description = "Business rule violated")
  })
  @PostMapping("/order/resale/{uuid}")
  public ResponseEntity<PedidoFornecedorResponse> enviarPedidoFornecedor(
      @PathVariable String uuid) {
    return ResponseEntity.ok(fornecedorService.enviarPedidoFornecedor(UUID.fromString(uuid)));
  }
}
