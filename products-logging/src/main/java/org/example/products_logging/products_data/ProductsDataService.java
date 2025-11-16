package org.example.products_logging.products_data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.products_logging.config.exceptions.ProductLogNotFound;
import org.example.products_logging.config.records.PaginatedResponse;
import org.example.products_logging.products_data.records.ProductsDataDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsDataService {
    private final ProductsDataRepository productsDataRepository;

    public PaginatedResponse<ProductsData> getPaginatedProductsDate(Pageable pageable) {
        Page<ProductsData> allDataPaged = productsDataRepository.findAll(pageable);

        return new PaginatedResponse<>(
                allDataPaged.getContent(),
                allDataPaged.getNumber(),
                allDataPaged.getSize(),
                allDataPaged.getTotalElements(),
                allDataPaged.getTotalPages(),
                allDataPaged.isLast()
        );
    }

    public boolean logData(ProductsDataDto dto) {
        try {
            if (!validateProductExistence(dto)) {
                throw new ProductLogNotFound(String.format("Product log for product with id: %s was not found. Event type: %s",
                        dto.productId(),
                        dto.event()));
            }

            ProductsData data = new ProductsData();

            data.setProductId(UUID.fromString(dto.productId()));
            data.setTimestamp(LocalDateTime.now());
            data.setEventType(EventType.valueOf(dto.event()));
            data.setPriceChange(dto.price());
            data.setQuantityChange(dto.quantity());

            productsDataRepository.save(data);

            return true;
        } catch (IllegalArgumentException e) {
            log.error("Error setting uuid or event type: {}", e.getMessage());

            return false;
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage());

            return false;
        }
    }

    private boolean validateProductExistence(ProductsDataDto dto) {
        EventType type = EventType.valueOf(dto.event());

        if (type == EventType.UPDATED || type == EventType.DELETED) {
            return productsDataRepository.findFirstProductsDataByProductId(UUID.fromString(dto.productId())).isPresent();
        }

        return true;
    }
}
