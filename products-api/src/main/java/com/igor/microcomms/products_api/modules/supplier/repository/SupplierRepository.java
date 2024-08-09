package com.igor.microcomms.products_api.modules.supplier.repository;

import com.igor.microcomms.products_api.modules.supplier.model.Supplier;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    List<Supplier> findByNameIgnoreCaseContaining(String name);
}
