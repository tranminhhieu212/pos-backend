package com.pos.payload.mapper;

import com.pos.modal.Store;
import com.pos.payload.dto.StoreDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StoreMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
//            @Mapping(target = "createdAt", ignore = true),
//            @Mapping(target = "updatedAt", ignore = true),
//            @Mapping(target = "status", ignore = true)
    })
    Store toEntity(StoreDto dto);

    StoreDto toDto(Store store);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "storeAdmin", ignore = true)
    })
    void updateFromDto(StoreDto dto, @MappingTarget Store store);
}