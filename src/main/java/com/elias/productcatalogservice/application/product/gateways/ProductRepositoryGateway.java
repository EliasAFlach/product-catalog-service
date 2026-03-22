package com.elias.productcatalogservice.application.product.gateways;

import com.elias.productcatalogservice.domain.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryGateway {
    Product save(Product product);
    Optional<Product> findActiveByProductId(UUID productId);
    List<Product> findAllActive();
    List<Product> findAllVersionsByProductId(UUID productId);
    void deactivateCurrentVersion(UUID productId);
}