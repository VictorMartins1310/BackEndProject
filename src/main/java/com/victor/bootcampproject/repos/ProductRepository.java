package com.victor.bootcampproject.repos;

import com.victor.bootcampproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByProductID(Long id);
}