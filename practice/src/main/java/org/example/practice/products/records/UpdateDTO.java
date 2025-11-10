package org.example.practice.products.records;

public record UpdateDTO(
        Double price,
        Integer quantity,
        String description,
        String name,
        String type
) {

}
