package com.elias.productcatalogservice.application.product.usecases;

import com.elias.productcatalogservice.application.product.gateways.ProductRepositoryGateway;
import com.elias.productcatalogservice.domain.product.Product;

import java.util.List;
import java.util.UUID;

public class GetProductUseCase {

    private final ProductRepositoryGateway productRepository;

    public GetProductUseCase(ProductRepositoryGateway productRepository) {
        this.productRepository = productRepository;
    }

    public Product findByProductId(UUID productId) {
        return productRepository.findActiveByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
    }

    public List<Product> findAll() {
        return productRepository.findAllActive();
    }

    public List<Product> findVersionHistory(UUID productId) {
        return productRepository.findAllVersionsByProductId(productId);
    }
}