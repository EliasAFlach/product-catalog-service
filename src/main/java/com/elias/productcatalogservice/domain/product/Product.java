package com.elias.productcatalogservice.domain.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Product {

    private UUID id;

    private UUID productId;

    private int version;
    private boolean active;

    private String name;
    private String description;
    private ProductType productType;
    private ProductRiskLevel riskLevel;
    private ProductStatus status;

    private BigDecimal interestRate;
    private BigDecimal minimumInvestment;
    private int gracePeriodDays;
    private int liquidityDays;

    private Instant createdAt;
    private Instant updatedAt;


    private Product(String name, String description, ProductType productType,
                    ProductRiskLevel riskLevel, BigDecimal interestRate,
                    BigDecimal minimumInvestment, int gracePeriodDays, int liquidityDays) {
        validate(name, productType, riskLevel, interestRate, minimumInvestment, gracePeriodDays, liquidityDays);
        this.id = UUID.randomUUID();
        this.productId = UUID.randomUUID();
        this.version = 1;
        this.active = true;
        this.name = name;
        this.description = description;
        this.productType = productType;
        this.riskLevel = riskLevel;
        this.status = ProductStatus.ACTIVE;
        this.interestRate = interestRate;
        this.minimumInvestment = minimumInvestment;
        this.gracePeriodDays = gracePeriodDays;
        this.liquidityDays = liquidityDays;
        this.createdAt = Instant.now();
    }

    private Product(UUID id, UUID productId, int version, boolean active,
                    String name, String description, ProductType productType,
                    ProductRiskLevel riskLevel, ProductStatus status,
                    BigDecimal interestRate, BigDecimal minimumInvestment,
                    int gracePeriodDays, int liquidityDays,
                    Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.productId = productId;
        this.version = version;
        this.active = active;
        this.name = name;
        this.description = description;
        this.productType = productType;
        this.riskLevel = riskLevel;
        this.status = status;
        this.interestRate = interestRate;
        this.minimumInvestment = minimumInvestment;
        this.gracePeriodDays = gracePeriodDays;
        this.liquidityDays = liquidityDays;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Product create(String name, String description, ProductType productType,
                                 ProductRiskLevel riskLevel, BigDecimal interestRate,
                                 BigDecimal minimumInvestment, int gracePeriodDays, int liquidityDays) {
        return new Product(name, description, productType, riskLevel,
                interestRate, minimumInvestment, gracePeriodDays, liquidityDays);
    }

    public static Product restore(UUID id, UUID productId, int version, boolean active,
                                  String name, String description, ProductType productType,
                                  ProductRiskLevel riskLevel, ProductStatus status,
                                  BigDecimal interestRate, BigDecimal minimumInvestment,
                                  int gracePeriodDays, int liquidityDays,
                                  Instant createdAt, Instant updatedAt) {
        return new Product(id, productId, version, active, name, description, productType,
                riskLevel, status, interestRate, minimumInvestment, gracePeriodDays,
                liquidityDays, createdAt, updatedAt);
    }

    public Product newVersion(String name, String description, ProductRiskLevel riskLevel,
                              BigDecimal interestRate, BigDecimal minimumInvestment,
                              int gracePeriodDays, int liquidityDays) {
        validate(name, this.productType, riskLevel, interestRate, minimumInvestment, gracePeriodDays, liquidityDays);
        Product next = new Product(
                UUID.randomUUID(),
                this.productId,
                this.version + 1,
                true,
                name,
                description,
                this.productType,
                riskLevel,
                this.status,
                interestRate,
                minimumInvestment,
                gracePeriodDays,
                liquidityDays,
                Instant.now(),
                null
        );
        return next;
    }

    public void deactivate() {
        if (this.status == ProductStatus.DISCONTINUED) {
            throw new IllegalStateException("Cannot deactivate a discontinued product");
        }
        this.status = ProductStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        if (this.status == ProductStatus.DISCONTINUED) {
            throw new IllegalStateException("Cannot activate a discontinued product");
        }
        this.status = ProductStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void discontinue() {
        if (this.status == ProductStatus.DISCONTINUED) return;
        this.status = ProductStatus.DISCONTINUED;
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public void markInactive() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public boolean isAvailableForOrders() {
        return this.status == ProductStatus.ACTIVE && this.active;
    }

    private static void validate(String name, ProductType productType, ProductRiskLevel riskLevel,
                                 BigDecimal interestRate, BigDecimal minimumInvestment,
                                 int gracePeriodDays, int liquidityDays) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name is required");
        if (productType == null)
            throw new IllegalArgumentException("productType is required");
        if (riskLevel == null)
            throw new IllegalArgumentException("riskLevel is required");
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("interestRate must be greater than zero");
        if (minimumInvestment == null || minimumInvestment.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("minimumInvestment must be greater than zero");
        if (gracePeriodDays < 0)
            throw new IllegalArgumentException("gracePeriodDays cannot be negative");
        if (liquidityDays < 0)
            throw new IllegalArgumentException("liquidityDays cannot be negative");
    }

    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public int getVersion() { return version; }
    public boolean isActive() { return active; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ProductType getProductType() { return productType; }
    public ProductRiskLevel getRiskLevel() { return riskLevel; }
    public ProductStatus getStatus() { return status; }
    public BigDecimal getInterestRate() { return interestRate; }
    public BigDecimal getMinimumInvestment() { return minimumInvestment; }
    public int getGracePeriodDays() { return gracePeriodDays; }
    public int getLiquidityDays() { return liquidityDays; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}