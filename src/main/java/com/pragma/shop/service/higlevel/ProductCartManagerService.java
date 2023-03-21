package com.pragma.shop.service.higlevel;

import com.pragma.shop.constant.Constant;
import com.pragma.shop.dto.cart.ChangeCartDto;
import com.pragma.shop.entity.AppUser;
import com.pragma.shop.entity.Product;
import com.pragma.shop.entity.UserCart;
import com.pragma.shop.service.entity.ProductService;
import com.pragma.shop.service.entity.UserCartService;
import com.pragma.shop.service.lowlevel.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCartManagerService {

    private final SecurityService securityService;
    private final UserCartService userCartService;
    private final ProductService productService;

    @Transactional
    public void addToCart(ChangeCartDto changeCartDto, String token) {
        AppUser currentUser = securityService.getUserWithToken(token);

        long productId = changeCartDto.getProductId();
        int productCount = changeCartDto.getCount();

        Product product = productService.findById(productId).orElseThrow(() ->Problem.valueOf(Status.BAD_REQUEST, Constant.PRODUCT_NOT_EXIST));

        log.debug("UserId: {}, want to add productId: {}, to cart", currentUser.getId(), productId);
        Optional<UserCart> userCartOptional = userCartService.findByUserIdAndProductId(currentUser.getId(), productId);

        if (userCartOptional.isEmpty()) {
            userCartService.create(currentUser.getId(), product, productCount);
        }
        else {
            userCartService.increaseAmount(userCartOptional.get(), productCount);
        }
        productService.decreaseAmount(product, productCount);
    }

    @Transactional
    public void removeFromCart(ChangeCartDto changeCartDto, String token) {
        AppUser currentUser = securityService.getUserWithToken(token);

        long productId = changeCartDto.getProductId();
        int productCount = changeCartDto.getCount();

        Product product = productService.findById(productId).orElseThrow(() ->Problem.valueOf(Status.BAD_REQUEST, Constant.PRODUCT_NOT_EXIST));

        log.debug("UserId: {}, want to add productId: {}, to cart", currentUser.getId(), productId);
        Optional<UserCart> userCartOptional = userCartService.findByUserIdAndProductId(currentUser.getId(), productId);

        if (userCartOptional.isPresent()) {
            userCartService.decreaseAmount(userCartOptional.get(), productCount);
            productService.increaseAmount(product, productCount);
        }
        else {
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.CART_NOT_EXIST);
        }
    }
}
