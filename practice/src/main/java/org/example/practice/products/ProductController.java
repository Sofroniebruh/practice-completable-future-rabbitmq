package org.example.practice.products;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.practice.products.records.ProductDTO;
import org.example.practice.products.records.UpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(productService.getProductbyId(id));
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@Valid ProductDTO dto) {
        return ResponseEntity.ok().body(productService.saveProduct(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @Valid UpdateDTO dto) {
        return ResponseEntity.ok().body(productService.updateProductById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable UUID id) {
        return ResponseEntity.ok().body(productService.deleteById(id));
    }
}
