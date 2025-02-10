package com.github.nielsonrocha.challenge.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "revendas")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RevendaEntity {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private UUID uuid;

  @Column(nullable = false, unique = true, length = 14)
  private String cnpj;

  @Column(nullable = false, name = "razao_social")
  private String razaoSocial;

  @Column(nullable = false, name = "nome_fantasia")
  private String nomeFantasia;

  @Column(nullable = false, unique = true)
  private String email;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "revenda", cascade = CascadeType.PERSIST, orphanRemoval = true)
  @BatchSize(size = 10)
  private Set<TelefoneEntity> telefones;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "revenda", cascade = CascadeType.PERSIST, orphanRemoval = true)
  @BatchSize(size = 10)
  private Set<EnderecoEntity> enderecos;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "revenda", cascade = CascadeType.PERSIST, orphanRemoval = true)
  @BatchSize(size = 10)
  private Set<ContatoEntity> contatos;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }
}
