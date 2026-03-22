package com.elias.productcatalogservice.application.product.usecases;

import com.elias.productcatalogservice.application.product.gateways.ProductEventPublisherGateway;
import com.elias.productcatalogservice.application.product.gateways.ProductRepositoryGateway;
import com.elias.productcatalogservice.domain.product.Product;
import com.elias.productcatalogservice.domain.product.ProductStatus;

import java.util.UUID;

public class ChangeProductStatusUseCase {

    private final ProductRepositoryGateway productRepository;
    private final ProductEventPublisherGateway eventPublisher;

    public ChangeProductStatusUseCase(ProductRepositoryGateway productRepository,
                                      ProductEventPublisherGateway eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    public void execute(UUID productId, ProductStatus newStatus, UUID correlationId) {
        Product product = productRepository.findActiveByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        switch (newStatus) {
            case ACTIVE -> product.activate();
            case INACTIVE -> product.deactivate();
            case DISCONTINUED -> product.discontinue();
        }

        productRepository.save(product);
        eventPublisher.publishProductUpdated(product, correlationId);
    }
}