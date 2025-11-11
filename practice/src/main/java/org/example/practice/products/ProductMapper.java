package org.example.practice.products;

import org.example.practice.products.records.UpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "price", target = "productPrice")
    @Mapping(source = "description", target = "productDescription")
    @Mapping(source = "type", target = "type")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(UpdateDTO dto, @MappingTarget Product product);
}
