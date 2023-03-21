package com.pragma.shop.controller.rest;

import com.pragma.shop.constant.Constant;
import com.pragma.shop.dto.UserCreateDto;
import com.pragma.shop.service.entity.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constant.BASE_URL + Constant.VERSION + "/users")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDto createDto) {
        log.info("REST request to create User: {}", createDto);
        userService.create(createDto);
        return ResponseEntity.created(URI.create("/users")).build();
    }
}
