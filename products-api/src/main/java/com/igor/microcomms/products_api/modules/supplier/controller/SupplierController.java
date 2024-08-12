package com.igor.microcomms.products_api.modules.supplier.controller;

import com.igor.microcomms.products_api.config.exception.SuccessResponse;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierRequest;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierResponse;
import com.igor.microcomms.products_api.modules.supplier.service.SupplierService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
@AllArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/")
    public List<SupplierResponse> findAll() {
        return supplierService.findAll();
    }

    @GetMapping("{id}")
    public SupplierResponse findById(@PathVariable(value = "id") Integer id) {
        return supplierService.findIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<SupplierResponse> findByName(@PathVariable(value = "name") String name) {
        return supplierService.findByName(name);
    }

    @PostMapping("/")
    public SupplierResponse save(@RequestBody SupplierRequest request) {
        return supplierService.save(request);
    }

    @PutMapping("{id}")
    public SupplierResponse update(@PathVariable(value = "id") Integer id, @RequestBody SupplierRequest request) {
        return supplierService.update(id, request);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable(value = "id") Integer id) {
        return supplierService.delete(id);
    }

}
