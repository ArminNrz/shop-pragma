package com.pragma.shop.repository;

import com.pragma.shop.entity.UserCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCartRepository extends JpaRepository<UserCart, Long> {
    Optional<UserCart> findByAppUserIdAndProductIdAndActiveIsTrue(long userId, long productId);
    List<UserCart> findAllByAppUserIdAndActiveIsTrue(long userId);
}
