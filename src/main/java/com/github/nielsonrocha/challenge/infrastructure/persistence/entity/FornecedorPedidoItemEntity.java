package com.github.nielsonrocha.challenge.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FORNECEDOR_PEDIDO_ITENS")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class FornecedorPedidoItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  @EqualsAndHashCode.Include
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "PEDIDO_ID", nullable = false)
  private PedidoEntity pedido;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "FORNECEDOR_PEDIDO_ID", nullable = false)
  private FornecedorPedidoEntity fornecedorPedido;

  @Size(max = 255)
  @NotNull
  @Column(name = "PRODUTO", nullable = false)
  private String produto;

  @NotNull
  @Column(name = "QUANTIDADE", nullable = false)
  private Long quantidade;
}
