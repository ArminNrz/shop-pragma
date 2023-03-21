package com.pragma.shop.dto.cart;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartDto {
    private String productName;
    private int count;
    private BigDecimal eachPrice;
    private BigDecimal totalPrice;
}
