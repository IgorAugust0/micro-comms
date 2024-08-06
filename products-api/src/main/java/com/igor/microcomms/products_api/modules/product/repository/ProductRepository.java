package com.igor.microcomms.products_api.modules.product.repository;

import com.igor.microcomms.products_api.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
