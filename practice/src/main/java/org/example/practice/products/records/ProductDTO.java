package org.example.practice.products.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDTO(
        @NotNull(message = "Price is required") double price,
        @NotNull(message = "Quantity is required") int quantity,
        @NotBlank(message = "Description is required") String description,
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Type is required") String type
) {

}
