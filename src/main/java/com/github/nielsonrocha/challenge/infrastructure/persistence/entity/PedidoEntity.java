package com.github.nielsonrocha.challenge.infrastructure.persistence.entity;

import com.github.nielsonrocha.challenge.domain.model.PedidoStatus;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pedidos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PedidoEntity {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private UUID uuid;

  @Enumerated(EnumType.STRING)
  private PedidoStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id")
  private ClienteEntity cliente;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "revenda_id")
  private RevendaEntity revenda;

  @Column(name = "data_criacao", nullable = false)
  private LocalDateTime dataCriacao;

  @Column(name = "data_atualizacao", nullable = false)
  private LocalDateTime dataAtualizacao;

  @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemPedidoEntity> itens;

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
