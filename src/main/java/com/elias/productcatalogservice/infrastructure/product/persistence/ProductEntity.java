package com.elias.productcatalogservice.infrastructure.product.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_products")
@Getter
@Setter
class ProductEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String productType;

    @Column(nullable = false)
    private String riskLevel;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private BigDecimal minimumInvestment;

    @Column(nullable = false)
    private Integer gracePeriodDays;

    @Column(nullable = false)
    private Integer liquidityDays;

    @Column(nullable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;
}