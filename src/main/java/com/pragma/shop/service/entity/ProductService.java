package com.pragma.shop.service.entity;

import com.pragma.shop.constant.Constant;
import com.pragma.shop.dto.ProductDto;
import com.pragma.shop.entity.Product;
import com.pragma.shop.mapper.ProductMapper;
import com.pragma.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public List<ProductDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto).stream().collect(Collectors.toList());
    }

    public Optional<Product> findById(long id) {
        return repository.findById(id);
    }

    public void decreaseAmount(Product product, int productCount) {
        if (product.getAvailableCount() < productCount) {
            log.error("Product id: {}, is not enough for this order", product.getId());
            throw Problem.valueOf(Status.BAD_REQUEST, Constant.PRODUCT_NOT_ENOUGH);
        }
        product.setAvailableCount(product.getAvailableCount() - productCount);
        repository.save(product);
    }

    public void increaseAmount(Product product, int productCount) {
        product.setAvailableCount(product.getAvailableCount() + productCount);
        repository.save(product);
    }
}
