package org.example.practice.products;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.practice.config.exceptions.InternalErrorException;
import org.example.practice.config.exceptions.ProductNotFoundException;
import org.example.practice.products.records.ProductDTO;
import org.example.practice.products.records.UpdateDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public Product saveProduct(ProductDTO dto) {
        Product newProduct = Product.builder()
                .productName(dto.name())
                .productPrice(dto.price())
                .productDescription(dto.description())
                .type(ProductType.valueOf(dto.type().toUpperCase()))
                .quantity(dto.quantity())
                .build();

        return productRepository.save(newProduct);
    }

    public Product getProductbyId(UUID id) {
        try {
            return getProductOrException(id);
        } catch (ProductNotFoundException e) {
            log.error("Error retrieving product: {}", e.getMessage());

            throw e;
        } catch (Exception e) {
            log.error("Internal error while retrieving: {}", e.getMessage());

            throw new InternalErrorException();
        }
    }

    @Transactional
    public Product updateProductById(UUID id, UpdateDTO dto) {
        try {
            Product product = getProductOrException(id);

            productMapper.updateProductFromDto(dto, product);

            return productRepository.save(product);
        } catch (ProductNotFoundException e) {
            log.error("Error retrieving the product while updating: {}", e.getMessage());

            throw e;
        } catch (Exception e) {
            log.error("Internal error while updating: {}", e.getMessage());

            throw new InternalErrorException();
        }
    }

    public Product deleteById(UUID id) {
        try {
            Product product = getProductOrException(id);

            productRepository.deleteById(product.getId());

            return product;
        } catch (ProductNotFoundException e) {
            log.error("Error retrieving the product while deleting: {}", e.getMessage());

            throw e;
        } catch (Exception e) {
            log.error("Internal error while deleting: {}", e.getMessage());

            throw new InternalErrorException();
        }
    }

    private Product getProductOrException(UUID id) {
        return productRepository.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id: %s not found", id)));
    }
}
