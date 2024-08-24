package com.igor.microcomms.products_api.modules.product.controller;

import com.igor.microcomms.products_api.config.exception.SuccessResponse;
import com.igor.microcomms.products_api.modules.product.dto.ProductRequest;
import com.igor.microcomms.products_api.modules.product.dto.ProductResponse;
import com.igor.microcomms.products_api.modules.product.dto.ProductSalesResponse;
import com.igor.microcomms.products_api.modules.product.dto.ProductStockCheckRequest;
import com.igor.microcomms.products_api.modules.product.service.ProductService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("{id}")
    public ProductResponse findById(@PathVariable(value = "id") Integer id) {
        return productService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<ProductResponse> findByName(@PathVariable(value = "name") String name) {
        return productService.findByName(name);
    }

    @GetMapping("supplier/{supplierId}")
    public List<ProductResponse> findBySupplierId(@PathVariable(value = "supplierId") Integer supplierId) {
        return productService.findBySupplierId(supplierId);
    }

    @GetMapping("category/{categoryId}")
    public List<ProductResponse> findByCategoryId(@PathVariable(value = "categoryId") Integer categoryId) {
        return productService.findByCategoryId(categoryId);
    }

    @GetMapping("{id}/sales")
    public ProductSalesResponse findSalesByProductId(@PathVariable(value = "id") Integer id) {
        return productService.findProductSales(id);
    }

    @PostMapping("/")
    public ProductResponse save(@RequestBody ProductRequest request) {
        return productService.save(request);
    }

    @PostMapping("check-stock")
    public SuccessResponse checkProductStock(@RequestBody ProductStockCheckRequest request) {
        return productService.checkProductStock(request);
    }

    @PutMapping("{id}")
    public ProductResponse update(@PathVariable(value = "id") Integer id, @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable(value = "id") Integer id) {
        return productService.delete(id);
    }
}
