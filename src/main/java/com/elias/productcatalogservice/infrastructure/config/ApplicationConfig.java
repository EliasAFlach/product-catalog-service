package com.elias.productcatalogservice.infrastructure.config;

import com.elias.productcatalogservice.application.product.gateways.ProductEventPublisherGateway;
import com.elias.productcatalogservice.application.product.gateways.ProductRepositoryGateway;
import com.elias.productcatalogservice.application.product.usecases.ChangeProductStatusUseCase;
import com.elias.productcatalogservice.application.product.usecases.CreateProductUseCase;
import com.elias.productcatalogservice.application.product.usecases.GetProductUseCase;
import com.elias.productcatalogservice.application.product.usecases.UpdateProductUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public CreateProductUseCase createProductUseCase(
            ProductRepositoryGateway productRepository,
            ProductEventPublisherGateway eventPublisher) {
        return new CreateProductUseCase(productRepository, eventPublisher);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(
            ProductRepositoryGateway productRepository,
            ProductEventPublisherGateway eventPublisher) {
        return new UpdateProductUseCase(productRepository, eventPublisher);
    }

    @Bean
    public ChangeProductStatusUseCase changeProductStatusUseCase(
            ProductRepositoryGateway productRepository,
            ProductEventPublisherGateway eventPublisher) {
        return new ChangeProductStatusUseCase(productRepository, eventPublisher);
    }

    @Bean
    public GetProductUseCase getProductUseCase(ProductRepositoryGateway productRepository) {
        return new GetProductUseCase(productRepository);
    }
}