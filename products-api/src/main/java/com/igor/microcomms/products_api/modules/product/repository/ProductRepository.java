package com.igor.microcomms.products_api.modules.product.repository;

import com.igor.microcomms.products_api.modules.product.model.Product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameIgnoreCaseContaining(String name);
    List<Product> findByCategoryId(Integer categoryId);
    List<Product> findBySupplierId(Integer supplierId);
    Boolean existsByCategoryId(Integer categoryId);
    Boolean existsBySupplierId(Integer supplierId);

    @Query("SELECT p.createdAt FROM Product p WHERE p.id = :id")
    LocalDateTime findCreatedAtById(@Param("id") Integer id);
    
    @Query("SELECT p.lastModified FROM Product p WHERE p.id = :id")
    LocalDateTime findLastModifiedById(@Param("id") Integer id);
}
