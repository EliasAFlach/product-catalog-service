package com.elias.productcatalogservice.application.product.usecases;

import com.elias.productcatalogservice.application.product.gateways.ProductEventPublisherGateway;
import com.elias.productcatalogservice.application.product.gateways.ProductRepositoryGateway;
import com.elias.productcatalogservice.domain.product.Product;
import com.elias.productcatalogservice.domain.product.ProductRiskLevel;
import com.elias.productcatalogservice.domain.product.ProductType;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateProductUseCase {

    private final ProductRepositoryGateway productRepository;
    private final ProductEventPublisherGateway eventPublisher;

    public CreateProductUseCase(ProductRepositoryGateway productRepository,
                                ProductEventPublisherGateway eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    public UUID execute(String name, String description, ProductType productType,
                        ProductRiskLevel riskLevel, BigDecimal interestRate,
                        BigDecimal minimumInvestment, int gracePeriodDays,
                        int liquidityDays, UUID correlationId) {

        Product product = Product.create(name, description, productType, riskLevel,
                interestRate, minimumInvestment, gracePeriodDays, liquidityDays);

        productRepository.save(product);
        eventPublisher.publishProductCreated(product, correlationId);

        return product.getProductId();
    }
}