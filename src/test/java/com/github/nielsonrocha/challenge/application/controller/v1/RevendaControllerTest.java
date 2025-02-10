package com.github.nielsonrocha.challenge.application.controller.v1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.nielsonrocha.challenge.domain.dto.PageDTO;
import com.github.nielsonrocha.challenge.domain.dto.RevendaRequest;
import com.github.nielsonrocha.challenge.domain.dto.RevendaResponse;
import com.github.nielsonrocha.challenge.domain.model.Revenda;
import com.github.nielsonrocha.challenge.domain.service.RevendaService;
import com.github.nielsonrocha.challenge.infrastructure.mapper.RevendaMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class RevendaControllerTest {

  private MockMvc mockMvc;

  @Mock private RevendaService revendaService;

  @Mock private RevendaMapper revendaMapper;

  @InjectMocks private RevendaController revendaController;

  private Revenda revenda;

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(revendaController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    revenda =
        new Revenda(
            1L,
            UUID.randomUUID(),
            "12345678000195",
            "Razão Social",
            "Nome Fantasia",
            "email@example.com",
            Set.of(),
            Set.of(),
            Set.of());
  }

  @Test
  void criarRevenda_DeveRetornar201_QuandoCriadoComSucesso() throws Exception {
    RevendaRequest request =
        new RevendaRequest(
            "CNPJ",
            "Razao Social",
            "Nome Fantasia",
            "email@test.com",
            List.of(),
            List.of(),
            List.of());
    RevendaResponse response =
        new RevendaResponse(
            UUID.randomUUID(),
            "CNPJ",
            "Razao Social",
            "Nome Fantasia",
            "email@test.com",
            Set.of(),
            Set.of(),
            Set.of());

    when(revendaService.criarRevenda(any())).thenReturn(revenda);
    when(revendaMapper.toModel(any(RevendaRequest.class))).thenReturn(revenda);
    when(revendaMapper.toResponse(any())).thenReturn(response);

    mockMvc
        .perform(
            post("/v1/resales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n"
                        + "  \"cnpj\": \"89007550000146\",\n"
                        + "  \"companyName\": \"Teste\",\n"
                        + "  \"tradeName\": \"Teste Inc.\",\n"
                        + "  \"email\": \"teste@teste.com\",\n"
                        + "  \"phones\": [\n"
                        + "    {\n"
                        + "      \"areaCode\": \"98\",\n"
                        + "      \"number\": \"987391388\"\n"
                        + "    }\n"
                        + "  ],\n"
                        + "  \"contacts\": [\n"
                        + "    {\n"
                        + "      \"name\": \"Nielson\",\n"
                        + "      \"main\": true\n"
                        + "    }\n"
                        + "  ],\n"
                        + "  \"addresses\": [\n"
                        + "    {\n"
                        + "      \"street\": \"Rua E\",\n"
                        + "      \"number\": \"15\",\n"
                        + "      \"complement\": \"Qd 15\",\n"
                        + "      \"district\": \"Jardim Turu\",\n"
                        + "      \"city\": \"São Luís\",\n"
                        + "      \"state\": \"MA\",\n"
                        + "      \"postalCode\": \"65058558\"\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}"))
        .andExpect(status().isCreated());
  }

  @Test
  void criarRevenda_DeveRetornar400_QuandoRequestInvalido() throws Exception {
    mockMvc
        .perform(post("/v1/resales").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void atualizarRevenda_DeveRetornar200_QuandoAtualizadoComSucesso() throws Exception {
    String uuid = UUID.randomUUID().toString();
    RevendaRequest request =
        new RevendaRequest(
            "CNPJ",
            "Razao Social",
            "Nome Fantasia",
            "email@test.com",
            List.of(),
            List.of(),
            List.of());
    RevendaResponse response =
        new RevendaResponse(
            UUID.fromString(uuid),
            "CNPJ",
            "Razao Social",
            "Nome Fantasia",
            "email@test.com",
            Set.of(),
            Set.of(),
            Set.of());

    when(revendaService.atualizarRevenda(eq(uuid), any())).thenReturn(revenda);
    when(revendaMapper.toModel(any(RevendaRequest.class))).thenReturn(revenda);
    when(revendaMapper.toResponse(any())).thenReturn(response);

    mockMvc
        .perform(
            put("/v1/resales/" + uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\n"
                        + "  \"cnpj\": \"89007550000146\",\n"
                        + "  \"companyName\": \"Teste\",\n"
                        + "  \"tradeName\": \"Teste Inc.\",\n"
                        + "  \"email\": \"teste@teste.com\",\n"
                        + "  \"phones\": [\n"
                        + "    {\n"
                        + "      \"areaCode\": \"98\",\n"
                        + "      \"number\": \"987391388\"\n"
                        + "    }\n"
                        + "  ],\n"
                        + "  \"contacts\": [\n"
                        + "    {\n"
                        + "      \"name\": \"Nielson\",\n"
                        + "      \"main\": true\n"
                        + "    }\n"
                        + "  ],\n"
                        + "  \"addresses\": [\n"
                        + "    {\n"
                        + "      \"street\": \"Rua E\",\n"
                        + "      \"number\": \"15\",\n"
                        + "      \"complement\": \"Qd 15\",\n"
                        + "      \"district\": \"Jardim Turu\",\n"
                        + "      \"city\": \"São Luís\",\n"
                        + "      \"state\": \"MA\",\n"
                        + "      \"postalCode\": \"65058558\"\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}"))
        .andExpect(status().isOk());
  }

  @Test
  void atualizarRevenda_DeveRetornar400_QuandoRequestInvalido() throws Exception {
    String uuid = UUID.randomUUID().toString();

    mockMvc
        .perform(put("/v1/resales/" + uuid).contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void buscarTodasRevendas_DeveRetornar200() throws Exception {
    when(revendaService.buscarTodos(any(Pageable.class)))
        .thenReturn(new PageDTO<>(null, 0L, 0, 0, 0));

    mockMvc
        .perform(
            get("/v1/resales")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
