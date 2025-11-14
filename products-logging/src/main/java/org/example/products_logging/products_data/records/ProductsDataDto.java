package org.example.products_logging.products_data.records;

import java.math.BigDecimal;

public record ProductsDataDto(
    String event,
    int quantity,
    BigDecimal price,
    String productId
) {

}
