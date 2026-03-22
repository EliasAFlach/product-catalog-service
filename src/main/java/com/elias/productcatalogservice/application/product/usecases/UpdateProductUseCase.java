package com.elias.productcatalogservice.application.product.usecases;

import com.elias.productcatalogservice.application.product.gateways.ProductEventPublisherGateway;
import com.elias.productcatalogservice.application.product.gateways.ProductRepositoryGateway;
import com.elias.productcatalogservice.domain.product.Product;
import com.elias.productcatalogservice.domain.product.ProductRiskLevel;
import com.elias.productcatalogservice.domain.product.ProductStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateProductUseCase {

    private final ProductRepositoryGateway productRepository;
    private final ProductEventPublisherGateway eventPublisher;

    public UpdateProductUseCase(ProductRepositoryGateway productRepository,
                                ProductEventPublisherGateway eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    public void execute(UUID productId, String name, String description,
                        ProductRiskLevel riskLevel, BigDecimal interestRate,
                        BigDecimal minimumInvestment, int gracePeriodDays,
                        int liquidityDays, UUID correlationId) {

        Product current = productRepository.findActiveByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        if (current.getStatus() == ProductStatus.DISCONTINUED) {
            throw new IllegalStateException("Cannot update a discontinued product");
        }

        Product newVersion = current.newVersion(name, description, riskLevel,
                interestRate, minimumInvestment, gracePeriodDays, liquidityDays);

        productRepository.deactivateCurrentVersion(productId);
        productRepository.save(newVersion);
        eventPublisher.publishProductUpdated(newVersion, correlationId);
    }
}