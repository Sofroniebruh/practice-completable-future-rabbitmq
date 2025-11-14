package org.example.products_logging.config.exceptions;

public class ProductLogNotFound extends RuntimeException {
    public ProductLogNotFound(String message) {
        super(message);
    }
}
