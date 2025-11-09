package org.example.practice.products;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String productName;
    private String productDescription;
    private double productPrice;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private ProductType type;
}
