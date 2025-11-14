package org.example.practice.rabbit_services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.practice.config.RabbitConfig;
import org.example.practice.config.exceptions.InternalErrorException;
import org.example.practice.products.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class AsyncRabbitProductService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;
    private final Executor rabbitExecutor;

    public AsyncRabbitProductService(RabbitTemplate rabbitTemplate,
                                     ObjectMapper mapper,
                                     @Qualifier("asyncExecutor-rabbit") Executor rabbitExecutor) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
        this.rabbitExecutor = rabbitExecutor;
    }

    public CompletableFuture<Boolean> saveCreatedLogsToLogService(EventType event, Product product) {
        return CompletableFuture.supplyAsync(() -> {
            Object rabbitResponse = rabbitTemplate.convertSendAndReceive(
                    RabbitConfig.PRACTICE_EXCHANGE,
                    RabbitConfig.PRACTICE_ROUTING_KEY,
                    Map.of("event", event.toString(),
                            "quantity", product.getQuantity(),
                            "price", product.getProductPrice(),
                            "productId", product.getId().toString()
                    )
            );

            if (rabbitResponse != null) {
                Map<String, Object> responseMap = mapper.convertValue(
                        rabbitResponse,
                        new TypeReference<>() {});

                return validateResponse(responseMap);
            }

            return false;
        }, rabbitExecutor)
                .exceptionally((ex) -> {
                    Throwable cause = ex.getCause();

                    if (cause instanceof TimeoutException e) {
                        throw new InternalErrorException(
                                String.format("RabbitMq timed out: %s", e.getMessage()));
                    }

                    log.error("RabbitMq Internal Error: {}", ex.getMessage());

                    throw new InternalErrorException();
                });
    }

    private boolean validateResponse(Map<String, Object> response) {
        if (response.get("success") == null || response.get("success").equals("")) {
            return false;
        }

        return response.get("success").equals(true);
    }
}
