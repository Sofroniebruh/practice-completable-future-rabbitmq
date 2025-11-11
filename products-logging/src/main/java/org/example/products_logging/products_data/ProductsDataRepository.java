package org.example.products_logging.products_data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductsDataRepository extends JpaRepository<ProductsData, UUID> {

}
