CREATE TABLE revendas
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid          UUID UNIQUE         NOT NULL,
    cnpj          VARCHAR(14) UNIQUE  NOT NULL,
    razao_social  VARCHAR(255)        NOT NULL,
    nome_fantasia VARCHAR(255)        NOT NULL,
    email         VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE telefones
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid       UUID UNIQUE NOT NULL,
    ddd        VARCHAR(2)  NOT NULL,
    numero     VARCHAR(15) NOT NULL,
    revenda_id BIGINT      NOT NULL REFERENCES revendas (id) ON DELETE CASCADE
);

CREATE TABLE enderecos
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid        UUID UNIQUE  NOT NULL,
    logradouro  VARCHAR(255) NOT NULL,
    numero      VARCHAR(10)  NOT NULL,
    complemento VARCHAR(255),
    bairro      VARCHAR(255) NOT NULL,
    cidade      VARCHAR(255) NOT NULL,
    uf          VARCHAR(2)   NOT NULL,
    cep         VARCHAR(8)   NOT NULL,
    revenda_id  BIGINT       NOT NULL REFERENCES revendas (id) ON DELETE CASCADE
);

CREATE TABLE contatos
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid       UUID UNIQUE  NOT NULL,
    nome       VARCHAR(255) NOT NULL,
    principal  BOOLEAN      NOT NULL,
    revenda_id BIGINT       NOT NULL REFERENCES revendas (id) ON DELETE CASCADE
);

CREATE TABLE clientes
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid  UUID UNIQUE        NOT NULL,
    nome  VARCHAR(255)       NOT NULL,
    cpf   VARCHAR(11) UNIQUE NOT NULL,
    email VARCHAR(255)
);


CREATE TABLE pedidos
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid             UUID UNIQUE                                                           NOT NULL,
    cliente_id       BIGINT                                                                NOT NULL REFERENCES clientes (id) ON DELETE CASCADE,
    status           ENUM ('PENDENTE', 'AGUARDANDO_FORNECEDOR', 'FINALIZADO', 'CANCELADO') NOT NULL,
    data_criacao     TIMESTAMP                                                             NOT NULL,
    data_atualizacao TIMESTAMP                                                             NOT NULL,
    revenda_id       BIGINT                                                                NOT NULL REFERENCES revendas (id) ON DELETE CASCADE
);

CREATE TABLE itens_pedido
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id  BIGINT       NOT NULL,
    produto    VARCHAR(255) NOT NULL,
    quantidade INT          NOT NULL,
    CONSTRAINT FK_ITEM_PEDIDO_PEDIDO FOREIGN KEY (pedido_id) REFERENCES pedidos (id)
);

CREATE TABLE fornecedor_pedidos
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid             UUID UNIQUE                                          NOT NULL,
    revenda_id       BIGINT                                               NOT NULL REFERENCES revendas (id) ON DELETE CASCADE,
    status           ENUM ('EM_PROCESSAMENTO', 'FINALIZADO', 'CANCELADO') NOT NULL,
    id_externo       VARCHAR(255)                                         ,
    data_criacao     TIMESTAMP                                            NOT NULL,
    data_atualizacao TIMESTAMP                                            NOT NULL
);

CREATE TABLE fornecedor_pedido_itens
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id            BIGINT       NOT NULL,
    fornecedor_pedido_id BIGINT       NOT NULL,
    produto              VARCHAR(255) NOT NULL,
    quantidade           INT          NOT NULL,
    CONSTRAINT FK_FORNECEDOR_PEDIDO_ITEM_FORNECEDOR_PEDIDO FOREIGN KEY (fornecedor_pedido_id) REFERENCES fornecedor_pedidos (id),
    CONSTRAINT FK_FORNECEDOR_PEDIDO_ITEM_PEDIDO FOREIGN KEY (pedido_id) REFERENCES pedidos (id)
);

CREATE INDEX idx_pedidos_status ON pedidos (status);

CREATE INDEX idx_telefones_revenda_id ON telefones (revenda_id);

CREATE INDEX idx_enderecos_revenda_id ON enderecos (revenda_id);

CREATE INDEX idx_contatos_revenda_id ON contatos (revenda_id);

CREATE INDEX idx_pedidos_cliente_id ON pedidos (cliente_id);

CREATE INDEX idx_itens_pedido_pedido_id ON itens_pedido (pedido_id);

CREATE INDEX idx_fornecedor_pedidos_revenda_id ON fornecedor_pedidos (revenda_id);

CREATE INDEX idx_fornecedor_pedido_itens_fornecedor_pedido_id ON fornecedor_pedido_itens (fornecedor_pedido_id);