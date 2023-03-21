package com.pragma.shop.controller.rest;

import com.pragma.shop.constant.Constant;
import com.pragma.shop.dto.cart.ChangeCartDto;
import com.pragma.shop.service.higlevel.ProductCartManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(Constant.BASE_URL + Constant.VERSION + "/carts")
public class CartController {

    private final ProductCartManagerService productCartManagerService;

    @PostMapping("/add/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Void> addToCart(
            @PathVariable("productId") long productId,
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ChangeCartDto changeCartDto
            ) {
        log.info("REST request to add productId: {}, to cart", productId);
        changeCartDto.setProductId(productId);
        productCartManagerService.addToCart(changeCartDto, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable("productId") long productId,
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody ChangeCartDto changeCartDto
    ) {
        log.info("REST request to add productId: {}, to cart", productId);
        changeCartDto.setProductId(productId);
        productCartManagerService.removeFromCart(changeCartDto, token);
        return ResponseEntity.noContent().build();
    }
}
