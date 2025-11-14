package org.example.products_logging.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.products_logging.config.RabbitConfig;
import org.example.products_logging.products_data.ProductsDataService;
import org.example.products_logging.products_data.records.ProductsDataDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitListenerService {
    private final ObjectMapper mapper;
    private final ProductsDataService productsDataService;

    @RabbitListener(queues = RabbitConfig.PRACTICE_QUEUE)
    public Map<String, Object> consumeRabbitEvent(Map<String, Object> event) {
        try {
            ProductsDataDto receivedData = mapper.convertValue(event, ProductsDataDto.class);

            log.debug("Received data: {}, convertedData: {}", event, receivedData);

            if (!productsDataService.logData(receivedData)) return Map.of("success", false);
        } catch (IllegalArgumentException e) {
            log.error("Error converting received object to dto: {}", e.getMessage());

            return Map.of("success", false);
        }  catch (Exception e) {
            log.error("Internal error: {}", e.getMessage());

            return Map.of("success", false);
        }

        return Map.of("success", true);
    }
}
