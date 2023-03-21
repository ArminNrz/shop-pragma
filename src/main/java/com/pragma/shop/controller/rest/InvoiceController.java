package com.pragma.shop.controller.rest;

import com.pragma.shop.constant.Constant;
import com.pragma.shop.dto.InvoiceDto;
import com.pragma.shop.service.higlevel.InvoiceManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(Constant.BASE_URL + Constant.VERSION + "/invoice")
public class InvoiceController {

    private final InvoiceManagerService invoiceManagerService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<InvoiceDto> getInvoice(@RequestHeader("Authorization") String token) {
        log.info("REST request to get user carts");
        return ResponseEntity.ok(invoiceManagerService.getInvoice(token));
    }

    @PutMapping("/pay")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<Void> payInvoice(@RequestHeader("Authorization") String token) {
        log.info("REST request to pay invoice");
        invoiceManagerService.pay(token);
        return ResponseEntity.ok().build();
    }
}
