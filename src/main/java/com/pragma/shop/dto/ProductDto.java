package com.pragma.shop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int availableCount;
    private BigDecimal price;
}
