# Projeto Challenge

Este projeto é uma aplicação **Spring Boot** para gerenciar pedidos (`Pedidos`) de uma **revenda** (`Revenda`). Ele permite criar, atualizar e recuperar pedidos, além de integrar-se com um **fornecedor** (`Fornecedor`).

## Tecnologias Utilizadas

- Java
- Spring Boot
- Maven
- SQL
- **WireMock** (para simulação de serviços externos)
- **Docker Compose** (para orquestração de serviços)

## Estrutura do Projeto

- `application/exception`: Contém classes de exceção personalizadas e o manipulador global de exceções (`ResourceAdvice`).
- `domain/dto`: Contém os **DTOs** (Data Transfer Objects), como `PedidoFornecedorRequest` e `PedidoFornecedorResponse`.
- `domain/model`: Contém os modelos de domínio, como `Pedido`, `Revenda` e `ItemPedido`.
- `domain/service`: Contém interfaces e implementações dos serviços responsáveis pela lógica de negócios.
- `infrastructure/mapper`: Contém mapeadores para conversão entre entidades e DTOs.
- `infrastructure/persistence/repository`: Contém as interfaces de repositório para operações no banco de dados.

## Principais Classes e Métodos

### `PedidoServiceImpl` (Serviço de Pedidos)

Responsável pela lógica de negócio relacionada aos pedidos. Principais métodos:
- `criarPedido(UUID revendaUuid, Pedido pedido)`: Cria um novo pedido para uma revenda.
- `buscarPedidosPorRevenda(UUID revendaUuid, Pageable pageable)`: Retorna os pedidos de uma revenda com paginação.
- `buscarPorUuid(UUID uuid)`: Busca um pedido pelo seu **UUID**.
- `cancelarPedido(UUID uuid)`: Cancela um pedido pelo **UUID**.
- `alterarStatusPedidos(List<Pedido> pedidos, PedidoStatus status)`: Atualiza o status de múltiplos pedidos.

### `FornecedorServiceImpl` (Serviço de Fornecedor)

Gerencia a integração com o fornecedor. Principais métodos:
- `enviarPedidoFornecedor(UUID revendaUuid)`: Envia um pedido pendente para o fornecedor.
- `criarPedidoFornecedor(Revenda revenda, List<Pedido> pedidos)`: Gera um pedido para o fornecedor a partir dos pedidos pendentes.

### `ResourceAdvice` (Manipulador Global de Exceções)

Intercepta exceções personalizadas e retorna respostas **HTTP** apropriadas.

### `Revenda` (Modelo de Domínio)

Representa uma revenda, garantindo que apenas um contato seja marcado como principal.

## Como Executar a Aplicação

1. Clone o repositório.
2. Navegue até o diretório do projeto.
3. Execute `mvn clean install` para compilar o projeto.
4. Execute `mvn spring-boot:run` para iniciar a aplicação.
5. Para executar com **Docker Compose**, utilize:

   ```sh
   docker-compose up -d
   ```

## Principais Endpoints da API

- `POST /v1/orders/resale/{uuid}`: Cria um novo pedido para uma revenda.
- `GET /v1/orders/{uuid}`: Obtém um pedido pelo **UUID**.
- `GET /v1/orders/resale/{uuid}`: Lista todos os pedidos de uma revenda.
- `PATCH /v1/orders/{uuid}/cancel`: Cancela um pedido pelo **UUID**.
- `POST /v1/resales`: Cria uma nova revenda.
- `GET /v1/resales`: Lista todas as revendas.
- `PUT /v1/resales/{uuid}`: Atualiza uma revenda pelo **UUID**.
- `POST /v1/supplier/order`: Envia um pedido pendente para o fornecedor.

## Tratamento de Exceções

As exceções personalizadas são manipuladas por `ResourceAdvice`, retornando respostas de erro significativas:

- `BusinessException`: Retorna **HTTP 422 (Unprocessable Entity)**.
- `IntegrationException`: Retorna **HTTP 504 (Gateway Timeout)**.
- `NotFoundException`: Retorna **HTTP 404 (Not Found)**.

## Uso do WireMock com Docker Compose

Para simular a API do **Fornecedor**, utilizamos o **WireMock** no **Docker Compose**.

## Configuração do WireMock

Os mocks da API do **Fornecedor** são configurados dentro do diretório `wiremock`, utilizando JSONs de stub.

### Exemplo de stub (`wiremock/mappings/fornecedor-stub.json`)

```json
{
  "request": {
    "method": "POST",
    "url": "/api/v1/suppliers/order"
  },
  "response": {
    "status": 201,
    "body": "{ \"orderId\": \"12345\", \"message\": \"Pedido recebido\", \"status\": \"PROCESSING\" }",
    "headers": {
      "Content-Type": "application/json"
    }
  }
}
```

Com essa configuração, ao enviar um **POST** para `http://localhost:8081/api/v1/suppliers/order`, o **WireMock** retorna uma resposta simulada da API do fornecedor.

## Licença

Este projeto está licenciado sob a **Licença MIT**.  

