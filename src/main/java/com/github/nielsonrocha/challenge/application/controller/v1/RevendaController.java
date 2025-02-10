package com.github.nielsonrocha.challenge.application.controller.v1;

import com.github.nielsonrocha.challenge.domain.dto.PageDTO;
import com.github.nielsonrocha.challenge.domain.dto.RevendaRequest;
import com.github.nielsonrocha.challenge.domain.dto.RevendaResponse;
import com.github.nielsonrocha.challenge.domain.service.RevendaService;
import com.github.nielsonrocha.challenge.infrastructure.mapper.RevendaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Resale", description = "Operations related to Resales")
@RestController
@RequestMapping("/v1/resales")
@RequiredArgsConstructor
@Slf4j
public class RevendaController {

  private final RevendaService revendaService;
  private final RevendaMapper revendaMapper;

  @Operation(summary = "Create a new Resale")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Resale created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request"),
    @ApiResponse(responseCode = "422", description = "Business rule violated")
  })
  @PostMapping
  public ResponseEntity<RevendaResponse> criarRevenda(@RequestBody @Valid RevendaRequest request) {
    var revenda = revendaService.criarRevenda(revendaMapper.toModel(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(revendaMapper.toResponse(revenda));
  }

  @Operation(summary = "Update a Resale")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Resale updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request"),
    @ApiResponse(responseCode = "422", description = "Business rule violated")
  })
  @PutMapping("/{uuid}")
  public ResponseEntity<RevendaResponse> atualizarRevenda(
      @PathVariable String uuid, @RequestBody @Valid RevendaRequest request) {
    var revenda = revendaService.atualizarRevenda(uuid, revendaMapper.toModel(request));
    return ResponseEntity.ok(revendaMapper.toResponse(revenda));
  }

  @Operation(summary = "Get all Resales")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "List of Resale paginated"),
  })
  @GetMapping
  public ResponseEntity<PageDTO<RevendaResponse>> buscarTodasRevendas(
      @ParameterObject Pageable pageable) {
    return ResponseEntity.ok(revendaService.buscarTodos(pageable));
  }
}
