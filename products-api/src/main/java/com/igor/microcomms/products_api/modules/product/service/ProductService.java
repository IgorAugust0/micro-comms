package com.igor.microcomms.products_api.modules.product.service;

import com.igor.microcomms.products_api.config.exception.ValidationException;
import com.igor.microcomms.products_api.modules.category.service.CategoryService;
import com.igor.microcomms.products_api.modules.product.dto.ProductRequest;
import com.igor.microcomms.products_api.modules.product.dto.ProductResponse;
import com.igor.microcomms.products_api.modules.product.model.Product;
import com.igor.microcomms.products_api.modules.product.repository.ProductRepository;
import com.igor.microcomms.products_api.modules.supplier.service.SupplierService;

import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService,
            SupplierService supplierService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
    }

    // convert productRequest to Product and save it
    public ProductResponse save(ProductRequest request) {
        validateProductRequest(request);
        validateCategorySupplierId(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }

    private void validateProductRequest(ProductRequest request) {
        if (isEmpty(request.getName())) {
            throw new ValidationException("Product name was not provided");
        }
        if (isEmpty(request.getQuantityAvailable())) {
            throw new ValidationException("Product's available quantity was not provided");
        }
        if (request.getQuantityAvailable() <= 0) {
            throw new ValidationException("Product's available quantity must be greater than or equal to 0");
        }
        // if (isEmpty(request.getCategoryId()) || isEmpty(request.getSupplierId())) {
        //     validateCategorySupplierId(request);
        // }
    }

    private void validateCategorySupplierId(ProductRequest request) {
        if (isEmpty(request.getCategoryId())) {
            throw new ValidationException("Category ID was not provided");
        }
        if (isEmpty(request.getSupplierId())) {
            throw new ValidationException("Supplier ID was not provided");
        }
    }
}
