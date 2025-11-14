package org.example.products_logging.products_data;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "logging")
@Data
public class ProductsData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private UUID productId;
    private BigDecimal priceChange;
    private int quantityChange;
}
