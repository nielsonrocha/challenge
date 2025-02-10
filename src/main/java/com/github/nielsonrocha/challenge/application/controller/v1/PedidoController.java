package com.github.nielsonrocha.challenge.application.controller.v1;

import com.github.nielsonrocha.challenge.domain.dto.PedidoRequest;
import com.github.nielsonrocha.challenge.domain.dto.PedidoResponse;
import com.github.nielsonrocha.challenge.domain.service.PedidoService;
import com.github.nielsonrocha.challenge.infrastructure.mapper.PedidoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders", description = "Operations related to Orders")
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class PedidoController {

  private final PedidoService pedidoService;
  private final PedidoMapper pedidoMapper;

  @Operation(summary = "Create a new Order for a Resale")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Order created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request"),
    @ApiResponse(responseCode = "422", description = "Business rule violated")
  })
  @PostMapping("/resale/{uuid}")
  public ResponseEntity<PedidoResponse> criarPedido(
      @PathVariable String uuid, @RequestBody @Valid PedidoRequest pedidoRequest) {
    var pedido =
        pedidoService.criarPedido(UUID.fromString(uuid), pedidoMapper.fromRequest(pedidoRequest));
    return ResponseEntity.status(HttpStatus.CREATED).body(pedidoMapper.toResponse(pedido));
  }

  @Operation(summary = "Get an Order by UUID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Order found"),
    @ApiResponse(responseCode = "404", description = "Order not found")
  })
  @GetMapping("/{uuid}")
  public ResponseEntity<PedidoResponse> buscarPedido(@PathVariable String uuid) {
    var pedido = pedidoService.buscarPorUuid(UUID.fromString(uuid));
    return pedido
        .map(value -> ResponseEntity.ok(pedidoMapper.toResponse(value)))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @Operation(summary = "Get ALl Orders from a Resale by UUID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of Orders"),
  })
  @GetMapping("/resale/{uuid}")
  public ResponseEntity<Page<PedidoResponse>> buscarPedidoPorRevenda(
      @PathVariable String uuid, @ParameterObject Pageable pageable) {
    var pedidos = pedidoService.buscarPedidosPorRevenda(UUID.fromString(uuid), pageable);
    return ResponseEntity.ok(pedidos.map(pedidoMapper::toResponse));
  }

  @Operation(summary = "Cancel an Order by UUID")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Order canceled successfully"),
    @ApiResponse(responseCode = "404", description = "Order not found"),
    @ApiResponse(responseCode = "422", description = "Business rule violated")
  })
  @PatchMapping("/{uuid}/cancel")
  public ResponseEntity<Void> cancelarPedido(@PathVariable String uuid) {
    pedidoService.cancelarPedido(UUID.fromString(uuid));
    return ResponseEntity.noContent().build();
  }
}
