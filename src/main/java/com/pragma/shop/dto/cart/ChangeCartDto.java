package com.pragma.shop.dto.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pragma.shop.constant.Constant;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeCartDto {
    @JsonIgnore
    private long productId;
    @NotNull(message = Constant.PRODUCT_COUNT_EMPTY)
    private Integer count;
}
