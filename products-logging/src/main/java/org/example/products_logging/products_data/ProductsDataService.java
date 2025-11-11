package org.example.products_logging.products_data;

import lombok.RequiredArgsConstructor;
import org.example.products_logging.config.records.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductsDataService {
    private final ProductsDataRepository productsDataRepository;

    public PaginatedResponse<ProductsData> getPaginatedProductsDate(Pageable pageable) {
        Page<ProductsData> allDataPaged = productsDataRepository.getAll(pageable);

        return new PaginatedResponse<>(
                allDataPaged.getContent(),
                allDataPaged.getNumber(),
                allDataPaged.getSize(),
                allDataPaged.getTotalElements(),
                allDataPaged.getTotalPages(),
                allDataPaged.isLast()
        );
    }
}
