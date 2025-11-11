package org.example.products_logging.products_data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "logging")
@Data
public class ProductsData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private int productsCreated;
    private int productsQuantity;
    private double totalPrice;
    private int productsDeleted;
}
