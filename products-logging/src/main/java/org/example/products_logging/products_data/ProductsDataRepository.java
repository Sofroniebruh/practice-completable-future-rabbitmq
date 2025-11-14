package org.example.products_logging.products_data;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsDataRepository extends JpaRepository<ProductsData, UUID> {
    Page<ProductsData> findAll(@NonNull Pageable pageable);
    Optional<ProductsData> findProductsDataByProductId(UUID productId);
}
