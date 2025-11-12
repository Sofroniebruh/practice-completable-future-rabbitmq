package org.example.practice.products;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.practice.config.exceptions.InternalErrorException;
import org.example.practice.products.records.ProductDTO;
import org.example.practice.products.records.UpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Product>> saveProduct(@Valid @RequestBody ProductDTO dto) {
        return productService.saveProduct(dto)
                .thenApply(ResponseEntity::ok)
                .exceptionally((ex) -> {
                    throw new InternalErrorException(ex.getMessage());
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody UpdateDTO dto) {
        return ResponseEntity.ok().body(productService.updateProductById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable UUID id) {
        return ResponseEntity.ok().body(productService.deleteById(id));
    }
}
