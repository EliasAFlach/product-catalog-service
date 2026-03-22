package com.elias.productcatalogservice.infrastructure.product.gateways;

import com.elias.investcommon.event.product.ProductCreatedEvent;
import com.elias.investcommon.event.product.ProductUpdatedEvent;
import com.elias.productcatalogservice.application.product.gateways.ProductEventPublisherGateway;
import com.elias.productcatalogservice.domain.product.Product;
import com.elias.productcatalogservice.infrastructure.outbox.persistence.ProductOutboxEntity;
import com.elias.productcatalogservice.infrastructure.outbox.persistence.SpringDataProductOutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProductEventPublisher implements ProductEventPublisherGateway {

    private final SpringDataProductOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topics.product-created}")
    private String productCreatedTopic;

    @Value("${app.kafka.topics.product-updated}")
    private String productUpdatedTopic;

    @Override
    public void publishProductCreated(Product product, UUID correlationId) {
        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .eventId(UUID.randomUUID())
                .occurredOn(Instant.now())
                .schemaVersion("1")
                .correlationId(correlationId)
                .causationId(null)
                .productId(product.getProductId())
                .name(product.getName())
                .productType(product.getProductType().name())
                .riskLevel(product.getRiskLevel().name())
                .status(product.getStatus().name())
                .interestRate(product.getInterestRate())
                .minimumInvestment(product.getMinimumInvestment())
                .gracePeriodDays(product.getGracePeriodDays())
                .liquidityDays(product.getLiquidityDays())
                .version(product.getVersion())
                .build();

        saveToOutbox(productCreatedTopic, product.getProductId(), event);
        log.info("[OUTBOX] productCreated salvo. productId={} correlationId={}",
                product.getProductId(), correlationId);
    }

    @Override
    public void publishProductUpdated(Product product, UUID correlationId) {
        ProductUpdatedEvent event = ProductUpdatedEvent.builder()
                .eventId(UUID.randomUUID())
                .occurredOn(Instant.now())
                .schemaVersion("1")
                .correlationId(correlationId)
                .causationId(product.getProductId())
                .productId(product.getProductId())
                .name(product.getName())
                .productType(product.getProductType().name())
                .riskLevel(product.getRiskLevel().name())
                .status(product.getStatus().name())
                .interestRate(product.getInterestRate())
                .minimumInvestment(product.getMinimumInvestment())
                .gracePeriodDays(product.getGracePeriodDays())
                .liquidityDays(product.getLiquidityDays())
                .version(product.getVersion())
                .build();

        saveToOutbox(productUpdatedTopic, product.getProductId(), event);
        log.info("[OUTBOX] productUpdated salvo. productId={} version={} correlationId={}",
                product.getProductId(), product.getVersion(), correlationId);
    }

    private void saveToOutbox(String topic, UUID eventKey, Object event) {
        try {
            ProductOutboxEntity outbox = new ProductOutboxEntity();
            outbox.setTopic(topic);
            outbox.setEventKey(eventKey.toString());
            outbox.setPayload(objectMapper.writeValueAsString(event));
            outbox.setCreatedAt(Instant.now());
            outboxRepository.save(outbox);
        } catch (Exception e) {
            log.error("[OUTBOX] Falha ao salvar evento. topic={} eventKey={}", topic, eventKey, e);
            throw new RuntimeException("Erro ao salvar evento no Outbox. topic=" + topic, e);
        }
    }
}