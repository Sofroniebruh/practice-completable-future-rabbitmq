package org.example.products_logging.products_data;

import lombok.RequiredArgsConstructor;
import org.example.products_logging.config.records.PaginatedResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products_logs")
@RequiredArgsConstructor
public class ProductsDataController {
    private final ProductsDataService productsDataService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<ProductsData>> getProductsData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PaginatedResponse<ProductsData> paginatedResponse = productsDataService.getPaginatedProductsDate(pageable);

        return ResponseEntity.ok(paginatedResponse);
    }
}
