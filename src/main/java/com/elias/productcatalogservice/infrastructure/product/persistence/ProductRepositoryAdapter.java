package com.elias.productcatalogservice.infrastructure.product.persistence;

import com.elias.productcatalogservice.application.product.gateways.ProductRepositoryGateway;
import com.elias.productcatalogservice.domain.product.Product;
import com.elias.productcatalogservice.domain.product.ProductRiskLevel;
import com.elias.productcatalogservice.domain.product.ProductStatus;
import com.elias.productcatalogservice.domain.product.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryGateway {

    private final SpringDataProductRepository springRepository;

    @Override
    public Product save(Product product) {
        springRepository.save(toEntity(product));
        return product;
    }

    @Override
    public Optional<Product> findActiveByProductId(UUID productId) {
        return springRepository.findByProductIdAndActiveTrue(productId).map(this::toDomain);
    }

    @Override
    public List<Product> findAllActive() {
        return springRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Product> findAllVersionsByProductId(UUID productId) {
        return springRepository.findByProductIdOrderByVersionDesc(productId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deactivateCurrentVersion(UUID productId) {
        springRepository.deactivateCurrentVersion(productId);
    }

    private ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setProductId(product.getProductId());
        entity.setVersion(product.getVersion());
        entity.setActive(product.isActive());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setProductType(product.getProductType().name());
        entity.setRiskLevel(product.getRiskLevel().name());
        entity.setStatus(product.getStatus().name());
        entity.setInterestRate(product.getInterestRate());
        entity.setMinimumInvestment(product.getMinimumInvestment());
        entity.setGracePeriodDays(product.getGracePeriodDays());
        entity.setLiquidityDays(product.getLiquidityDays());
        entity.setCreatedAt(product.getCreatedAt());
        entity.setUpdatedAt(product.getUpdatedAt());
        return entity;
    }

    private Product toDomain(ProductEntity entity) {
        return Product.restore(
                entity.getId(),
                entity.getProductId(),
                entity.getVersion(),
                entity.getActive(),
                entity.getName(),
                entity.getDescription(),
                ProductType.valueOf(entity.getProductType()),
                ProductRiskLevel.valueOf(entity.getRiskLevel()),
                ProductStatus.valueOf(entity.getStatus()),
                entity.getInterestRate(),
                entity.getMinimumInvestment(),
                entity.getGracePeriodDays(),
                entity.getLiquidityDays(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}