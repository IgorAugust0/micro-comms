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

import java.util.List;

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

    public List<ProductResponse> findAll() {
        return convertToResponse(productRepository.findAll());
    }

    public Product findById(Integer id) {
        validateNotEmpty(id, "Product ID is required");
        return productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Product not found"));
    }

    public ProductResponse findByIdResponse(Integer id) {
        return ProductResponse.of(findById(id));
    }

    public List<ProductResponse> findByName(String name) {
        validateNotEmpty(name, "Product name is required");
        return convertToResponse(productRepository.findByNameIgnoreCaseContaining(name));
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId) {
        validateNotEmpty(supplierId, "Supplier ID is required");
        return convertToResponse(productRepository.findBySupplierId(supplierId));
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId) {
        validateNotEmpty(categoryId, "Category ID is required");
        return convertToResponse(productRepository.findByCategoryId(categoryId));
    }

    private void validateProductRequest(ProductRequest request) {
        validateNotEmpty(request.getName(), "Product name was not provided");
        validateNotEmpty(request.getQuantityAvailable(), "Product's available quantity was not provided");
        if (request.getQuantityAvailable() <= 0) {
            throw new ValidationException("Product's available quantity must be greater than or equal to 0");
        }
    }

    private void validateCategorySupplierId(ProductRequest request) {
        validateNotEmpty(request.getCategoryId(), "Category ID was not provided");
        validateNotEmpty(request.getSupplierId(), "Supplier ID was not provided");
    }

    private void validateNotEmpty(Object value, String message) {
        if (isEmpty(value)) {
            throw new ValidationException(message);
        }
    }

    private List<ProductResponse> convertToResponse(List<Product> products) {
        return products.stream().map(ProductResponse::of).toList();
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

}
