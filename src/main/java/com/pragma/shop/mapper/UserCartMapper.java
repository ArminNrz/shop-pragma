package com.pragma.shop.mapper;

import com.pragma.shop.dto.cart.CartDto;
import com.pragma.shop.entity.UserCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface UserCartMapper {

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "orderCount", target = "count")
    @Mapping(source = "product.price", target = "eachPrice")
    @Mapping(source = "entity", target = "totalPrice", qualifiedByName = "calculateTotalPrice")
    CartDto toDto(UserCart entity);

    @Named("calculateTotalPrice")
    default BigDecimal calculateTotalPrice(UserCart userCart) {
        return userCart.getProduct().getPrice().multiply(BigDecimal.valueOf(userCart.getOrderCount()));
    }
}
