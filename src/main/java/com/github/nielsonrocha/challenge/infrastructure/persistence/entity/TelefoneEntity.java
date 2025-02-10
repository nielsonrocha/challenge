package com.github.nielsonrocha.challenge.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "telefones")
public class TelefoneEntity {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private UUID uuid;

  @Column(nullable = false)
  private String ddd;

  @Column(nullable = false)
  private String numero;

  @ManyToOne
  @JoinColumn(name = "revenda_id")
  @JsonManagedReference
  private RevendaEntity revenda;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }

}
