package com.pragma.shop.mapper;

import com.pragma.shop.dto.ProductDto;
import com.pragma.shop.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
}
