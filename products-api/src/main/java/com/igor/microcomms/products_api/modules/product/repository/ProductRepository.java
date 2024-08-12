package com.igor.microcomms.products_api.modules.product.repository;

import com.igor.microcomms.products_api.modules.product.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameIgnoreCaseContaining(String name);
    List<Product> findByCategoryId(Integer categoryId);
    List<Product> findBySupplierId(Integer supplierId);
    Boolean existsByCategoryId(Integer categoryId);
    Boolean existsBySupplierId(Integer supplierId);
}
