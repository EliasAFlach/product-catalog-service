package com.elias.productcatalogservice.infrastructure.product.controller;

import com.elias.productcatalogservice.application.product.usecases.ChangeProductStatusUseCase;
import com.elias.productcatalogservice.application.product.usecases.CreateProductUseCase;
import com.elias.productcatalogservice.application.product.usecases.GetProductUseCase;
import com.elias.productcatalogservice.application.product.usecases.UpdateProductUseCase;
import com.elias.productcatalogservice.domain.product.Product;
import com.elias.productcatalogservice.domain.product.ProductRiskLevel;
import com.elias.productcatalogservice.domain.product.ProductStatus;
import com.elias.productcatalogservice.domain.product.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final ChangeProductStatusUseCase changeProductStatusUseCase;
    private final GetProductUseCase getProductUseCase;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @RequestBody CreateProductRequest request
    ) {
        UUID id = createProductUseCase.execute(
                request.name(), request.description(),
                ProductType.valueOf(request.productType()),
                ProductRiskLevel.valueOf(request.riskLevel()),
                request.interestRate(), request.minimumInvestment(),
                request.gracePeriodDays(), request.liquidityDays(),
                parseOrGenerate(correlationId)
        );
        return ResponseEntity.created(URI.create("/api/v1/products/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(getProductUseCase.findAll().stream().map(this::toResponse).toList());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findById(@PathVariable UUID productId) {
        return ResponseEntity.ok(toResponse(getProductUseCase.findByProductId(productId)));
    }

    @GetMapping("/{productId}/history")
    public ResponseEntity<List<ProductResponse>> findHistory(@PathVariable UUID productId) {
        return ResponseEntity.ok(getProductUseCase.findVersionHistory(productId).stream().map(this::toResponse).toList());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> update(
            @PathVariable UUID productId,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @RequestBody UpdateProductRequest request
    ) {
        updateProductUseCase.execute(
                productId, request.name(), request.description(),
                ProductRiskLevel.valueOf(request.riskLevel()),
                request.interestRate(), request.minimumInvestment(),
                request.gracePeriodDays(), request.liquidityDays(),
                parseOrGenerate(correlationId)
        );
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable UUID productId,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @RequestBody ChangeStatusRequest request
    ) {
        changeProductStatusUseCase.execute(
                productId,
                ProductStatus.valueOf(request.status()),
                parseOrGenerate(correlationId)
        );
        return ResponseEntity.accepted().build();
    }

    public record CreateProductRequest(
            String name, String description, String productType,
            String riskLevel, BigDecimal interestRate, BigDecimal minimumInvestment,
            int gracePeriodDays, int liquidityDays) {}

    public record UpdateProductRequest(
            String name, String description, String riskLevel,
            BigDecimal interestRate, BigDecimal minimumInvestment,
            int gracePeriodDays, int liquidityDays) {}

    public record ChangeStatusRequest(String status) {}

    public record ProductResponse(
            UUID id, UUID productId, int version, boolean active,
            String name, String description, String productType,
            String riskLevel, String status, BigDecimal interestRate,
            BigDecimal minimumInvestment, int gracePeriodDays,
            int liquidityDays, Instant createdAt, Instant updatedAt) {}

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getProductId(), p.getVersion(), p.isActive(),
                p.getName(), p.getDescription(), p.getProductType().name(),
                p.getRiskLevel().name(), p.getStatus().name(), p.getInterestRate(),
                p.getMinimumInvestment(), p.getGracePeriodDays(), p.getLiquidityDays(),
                p.getCreatedAt(), p.getUpdatedAt());
    }

    private UUID parseOrGenerate(String correlationId) {
        try {
            return (correlationId == null || correlationId.isBlank())
                    ? UUID.randomUUID() : UUID.fromString(correlationId);
        } catch (IllegalArgumentException e) {
            return UUID.randomUUID();
        }
    }
}