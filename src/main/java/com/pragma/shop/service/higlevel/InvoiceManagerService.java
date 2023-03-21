package com.pragma.shop.service.higlevel;

import com.pragma.shop.dto.cart.CartDto;
import com.pragma.shop.dto.InvoiceDto;
import com.pragma.shop.entity.AppUser;
import com.pragma.shop.service.entity.UserCartService;
import com.pragma.shop.service.lowlevel.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InvoiceManagerService {

    private final SecurityService securityService;
    private final UserCartService userCartService;

    public InvoiceDto getInvoice(String token) {
        InvoiceDto invoiceDto = new InvoiceDto();

        AppUser currentUser = securityService.getUserWithToken(token);
        invoiceDto.setName(currentUser.getName());
        invoiceDto.setPhoneNumber(currentUser.getPhoneNumber());

        List<CartDto> carts = userCartService.findByUserId(currentUser.getId());
        invoiceDto.setCarts(carts);

        BigDecimal totalValue = BigDecimal.ZERO;
        int totalCount = 0;
        for (CartDto cart: carts) {
            totalValue = totalValue.add(cart.getTotalPrice());
            totalCount = totalCount + cart.getCount();
        }
        invoiceDto.setTotalValue(totalValue);
        invoiceDto.setTotalCount(totalCount);

        return invoiceDto;
    }

    public void pay(String token) {
        AppUser currentUser = securityService.getUserWithToken(token);
        userCartService.deactivateUserCarts(currentUser.getId());
        log.info("userId: {}, paid all invoices", currentUser.getId());
    }
}
