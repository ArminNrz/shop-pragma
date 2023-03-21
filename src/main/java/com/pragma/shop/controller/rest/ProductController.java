package com.pragma.shop.controller.rest;

import com.pragma.shop.constant.Constant;
import com.pragma.shop.dto.ProductDto;
import com.pragma.shop.service.entity.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constant.BASE_URL + Constant.VERSION + "/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getAll(Pageable pageable) {
        log.info("REST request to get all products with page: {}", pageable);
        return ResponseEntity.ok(productService.getAll(pageable));
    }
}
