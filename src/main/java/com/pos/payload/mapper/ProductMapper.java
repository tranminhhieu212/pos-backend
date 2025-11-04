package com.pos.payload.mapper;

import com.pos.modal.Product;
import com.pos.payload.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    Product toEntity(ProductDto dto);

    @Mapping(source = "store.id", target = "storeId")
    ProductDto toDto(Product product);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
    })
    void updateFromDto(ProductDto dto, @MappingTarget Product product);
}