package com.elias.productcatalogservice.infrastructure.outbox.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataProductOutboxRepository extends JpaRepository<ProductOutboxEntity, UUID> {
    List<ProductOutboxEntity> findTop100ByProcessedAtIsNullOrderByCreatedAtAsc();
}