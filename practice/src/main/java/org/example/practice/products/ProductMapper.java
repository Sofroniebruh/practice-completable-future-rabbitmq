package org.example.practice.products;

import org.example.practice.products.records.UpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "price", target = "productPrice")
    @Mapping(source = "description", target = "productDescription")
    @Mapping(source = "type", target = "type")
    void updateProductFromDto(UpdateDTO dto, @MappingTarget Product product);
}
