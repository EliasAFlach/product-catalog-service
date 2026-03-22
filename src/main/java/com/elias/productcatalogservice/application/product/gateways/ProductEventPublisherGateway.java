package com.elias.productcatalogservice.application.product.gateways;

import com.elias.productcatalogservice.domain.product.Product;

import java.util.UUID;

public interface ProductEventPublisherGateway {
    void publishProductCreated(Product product, UUID correlationId);
    void publishProductUpdated(Product product, UUID correlationId);
}