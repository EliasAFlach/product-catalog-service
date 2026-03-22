package com.elias.productcatalogservice.infrastructure.product.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringDataProductRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findByProductIdAndActiveTrue(UUID productId);

    List<ProductEntity> findByActiveTrueOrderByNameAsc();

    List<ProductEntity> findByProductIdOrderByVersionDesc(UUID productId);

    @Modifying
    @Query("UPDATE ProductEntity p SET p.active = false WHERE p.productId = :productId AND p.active = true")
    void deactivateCurrentVersion(@Param("productId") UUID productId);
}