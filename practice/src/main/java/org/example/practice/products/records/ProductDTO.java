package org.example.practice.products.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductDTO(
        @Positive(message = "Price must be positive") Double price,
        @Positive(message = "Quantity must be positive") Integer quantity,
        @NotBlank(message = "Description is required") String description,
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Type is required") String type
) {


}
