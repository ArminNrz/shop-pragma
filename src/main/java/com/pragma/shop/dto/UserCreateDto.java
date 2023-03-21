package com.pragma.shop.dto;

import com.pragma.shop.constant.Constant;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserCreateDto {
    @NotEmpty(message = Constant.APP_USER_NAME_EMPTY)
    private String name;
    @NotEmpty(message = Constant.APP_USER_PHONE_NUMBER_EMPTY)
    private String phoneNumber;
    @NotEmpty(message = Constant.APP_USER_PASSWORD_EMPTY)
    private String password;
}
