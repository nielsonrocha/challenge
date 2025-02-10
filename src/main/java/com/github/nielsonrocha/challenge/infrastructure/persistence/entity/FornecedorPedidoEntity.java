package com.github.nielsonrocha.challenge.infrastructure.persistence.entity;

import com.github.nielsonrocha.challenge.domain.model.FornecedorPedidoStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
@Table(name = "FORNECEDOR_PEDIDOS")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class FornecedorPedidoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "ID_EXTERNO")
  private String idExterno;

  @NotNull
  @Column(name = "UUID", nullable = false)
  private UUID uuid;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "REVENDA_ID", nullable = false)
  private RevendaEntity revenda;

  @NotNull
  @Column(name = "DATA_CRIACAO", nullable = false)
  private LocalDateTime dataCriacao;

  @NotNull
  @Column(name = "DATA_ATUALIZACAO", nullable = false)
  private LocalDateTime dataAtualizacao;

  @Enumerated(EnumType.STRING)
  private FornecedorPedidoStatus status;

  @OneToMany(mappedBy = "fornecedorPedido", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FornecedorPedidoItemEntity> itens;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
    this.dataCriacao = LocalDateTime.now();
    this.dataAtualizacao = this.dataCriacao;
  }

  @PreUpdate
  public void preUpdate() {
    this.dataAtualizacao = LocalDateTime.now();
  }
}
