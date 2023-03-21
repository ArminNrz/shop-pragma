package com.pragma.shop.service.entity;

import com.pragma.shop.constant.Constant;
import com.pragma.shop.dto.cart.CartDto;
import com.pragma.shop.entity.AppUser;
import com.pragma.shop.entity.Product;
import com.pragma.shop.entity.UserCart;
import com.pragma.shop.mapper.UserCartMapper;
import com.pragma.shop.repository.UserCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserCartService {

    private final UserCartRepository repository;
    private final UserCartMapper mapper;

    public Optional<UserCart> findByUserIdAndProductId(long userId, long productId) {
        return repository.findByAppUserIdAndProductIdAndActiveIsTrue(userId, productId);
    }

    public UserCart create(Long userId, Product product, int orderCount) {
        UserCart entity = new UserCart();
        AppUser appUser = new AppUser();
        appUser.setId(userId);
        entity.setAppUser(appUser);
        entity.setProduct(product);
        entity.setOrderCount(orderCount);
        entity.setActive(true);
        repository.save(entity);
        return entity;
    }

    public void increaseAmount(UserCart userCart, int productCount) {
        userCart.setOrderCount(userCart.getOrderCount() + productCount);
        repository.save(userCart);
    }

    public void decreaseAmount(UserCart userCart, int productCount) {
        if (userCart.getOrderCount() < productCount) {
            log.error("Cart id: {}, amount: {}, for this product decrease is not enough", userCart.getId(), userCart.getOrderCount());
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.CART_AMOUNT_NOT_ENOUGH);
        } else if (userCart.getOrderCount() == productCount) {
            repository.delete(userCart);
        }
        else {
            userCart.setOrderCount(userCart.getOrderCount() - productCount);
            repository.save(userCart);
        }
    }

    public List<CartDto> findByUserId(Long userId) {
        return repository.findAllByAppUserIdAndActiveIsTrue(userId).stream()
                .peek(userCart -> log.debug("Found for userId: {}, cart: {}", userId, userCart))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void deactivateUserCarts(Long userId) {
        List<UserCart> entities = repository.findAllByAppUserIdAndActiveIsTrue(userId).stream()
                .peek(userCart -> userCart.setActive(false))
                .collect(Collectors.toList());
        repository.saveAll(entities);
    }
}
