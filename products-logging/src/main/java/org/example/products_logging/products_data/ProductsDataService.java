package org.example.products_logging.products_data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductsDataService {
    private final ProductsDataRepository productsDataRepository;


}
