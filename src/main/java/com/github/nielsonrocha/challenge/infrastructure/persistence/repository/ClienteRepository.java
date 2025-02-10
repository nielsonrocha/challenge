package com.github.nielsonrocha.challenge.infrastructure.persistence.repository;

import com.github.nielsonrocha.challenge.infrastructure.persistence.entity.ClienteEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

  Optional<ClienteEntity> findByUuid(UUID uuid);
}
