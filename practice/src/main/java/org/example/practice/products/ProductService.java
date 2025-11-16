package org.example.practice.products;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.practice.config.exceptions.InternalErrorException;
import org.example.practice.config.exceptions.ProductNotFoundException;
import org.example.practice.products.records.ProductDTO;
import org.example.practice.products.records.UpdateDTO;
import org.example.practice.rabbit_services.AsyncRabbitProductService;
import org.example.practice.rabbit_services.EventType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final AsyncRabbitProductService asyncRabbitProductService;
    private final Executor mainExecutor;

    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper,
                          AsyncRabbitProductService asyncRabbitProductService,
                          @Qualifier("asyncExecutor-main") Executor mainExecutor) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.asyncRabbitProductService = asyncRabbitProductService;
        this.mainExecutor = mainExecutor;
    }

    public CompletableFuture<Product> saveProduct(ProductDTO dto) {
        CompletableFuture<Product> asyncSaveProduct = CompletableFuture.supplyAsync(() -> {
            Product product = Product.builder()
                    .productName(dto.name())
                    .productPrice(dto.price())
                    .productDescription(dto.description())
                    .type(ProductType.valueOf(dto.type().toUpperCase()))
                    .quantity(dto.quantity())
                    .build();

            return productRepository.save(product);
        }, mainExecutor).handle((result, ex) -> {
            if (ex != null) {
                Throwable cause = ex.getCause();

                if (cause instanceof IllegalArgumentException e) {
                    log.error("Error while saving product: {}", e.getMessage());

                    throw e;
                }

                throw new InternalErrorException();
            }

            return result;
        });

        return asyncSaveProduct.thenCompose(product ->
                        asyncRabbitProductService.saveCreatedLogsToLogService(EventType.CREATED, product)
                .thenApply(response -> {
                    if (response) {
                        log.debug("Message processed successfully on save");
                    } else {
                        log.error("Product saved successfully, failed to log data in product logger service");
                    }

                    return product;
                })
        );
    }

    public Product getProductById(UUID id) {
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
    public CompletableFuture<Product> updateProductById(UUID id, UpdateDTO dto) {
        CompletableFuture<Product> updatedProduct = CompletableFuture.supplyAsync(() -> {
            Product product = getProductOrException(id);

            productMapper.updateProductFromDto(dto, product);

            return productRepository.save(product);
        }).handle((product, ex) -> {
            if (ex != null) {
                Throwable cause = ex.getCause();

                if (cause instanceof ProductNotFoundException e) {
                    log.error("Error retrieving the product while updating: {}", e.getMessage());

                    throw e;
                }

                throw new InternalErrorException();
            }

            return product;
        });

        return updatedProduct.thenCompose(product ->
            asyncRabbitProductService.saveCreatedLogsToLogService(EventType.UPDATED, product)
                .thenApply((response) -> {
                    if (response) {
                        log.debug("Message processed successfully on update");
                    } else {
                        log.error("Product updated successfully, failed to log data in product logger service");
                    }

                    return product;
            })
        );
    }

    public Product deleteById(UUID id)
    {
        try
        {
            Product product = getProductOrException(id);

            productRepository.deleteById(product.getId());

            return product;
        }
        catch (ProductNotFoundException e)
        {
            log.error("Error retrieving the product while deleting: {}", e.getMessage());

            throw e;
        }
        catch (Exception e)
        {
            log.error("Internal error while deleting: {}", e.getMessage());

            throw new InternalErrorException();
        }
    }

    private Product getProductOrException(UUID id)
    {
        return productRepository.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id: %s not found", id)));
    }
}
