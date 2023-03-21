package com.pragma.shop.dto;

import com.pragma.shop.dto.cart.CartDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InvoiceDto {
    private String name;
    private String phoneNumber;
    private List<CartDto> carts;
    private BigDecimal totalValue;
    private int totalCount;
}
