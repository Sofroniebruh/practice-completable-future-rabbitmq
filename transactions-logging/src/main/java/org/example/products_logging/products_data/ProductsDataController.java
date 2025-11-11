package org.example.products_logging.products_data;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products_logs")
@RequiredArgsConstructor
public class ProductsDataController {
    private final ProductsDataService productsDataService;


}
