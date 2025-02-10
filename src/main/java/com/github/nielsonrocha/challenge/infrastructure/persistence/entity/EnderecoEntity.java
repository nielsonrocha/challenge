package com.github.nielsonrocha.challenge.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
@Table(name = "enderecos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EnderecoEntity {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private UUID uuid;

  private String logradouro;

  private String numero;

  private String complemento;

  private String bairro;

  private String cidade;

  private String uf;

  private String cep;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "revenda_id")
  private RevendaEntity revenda;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }
}
