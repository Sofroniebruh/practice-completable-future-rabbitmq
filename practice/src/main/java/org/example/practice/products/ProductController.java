package org.example.practice.products;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.practice.products.records.ProductDTO;
import org.example.practice.products.records.UpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody ProductDTO dto) {
        Product product = productService.saveProduct(dto).join();

        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody UpdateDTO dto) {
        Product updatedProduct = productService.updateProductById(id, dto).join();

        return ResponseEntity.ok().body(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable UUID id) {
        return ResponseEntity.ok().body(productService.deleteById(id));
    }
}
