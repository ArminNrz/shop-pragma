package com.pragma.shop.security.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorizationData {
    private String phoneNumber;
    private String[] roles;
}
