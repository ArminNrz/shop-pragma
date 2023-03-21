package com.pragma.shop.mapper;

import com.pragma.shop.dto.UserCreateDto;
import com.pragma.shop.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser toEntity(UserCreateDto createDto);

}
