package com.pos.payload.mapper;

import com.pos.modal.User;
import com.pos.payload.dto.AuthRequest;
import com.pos.payload.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "lastLoginAt", ignore = true)
    })
    User toEntity(UserDto dto);

    UserDto toDto(User user);

    // Update User từ AuthRequest (không map password)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "lastLoginAt", ignore = true)
    })
    void updateFromAuthRequest(AuthRequest request, @MappingTarget User user);
}