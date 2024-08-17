package com.igor.microcomms.products_api.modules.product.service;

import com.igor.microcomms.products_api.config.exception.SuccessResponse;
import com.igor.microcomms.products_api.config.exception.ValidationException;
import com.igor.microcomms.products_api.modules.category.service.CategoryService;
import com.igor.microcomms.products_api.modules.product.dto.ProductQuantityDTO;
import com.igor.microcomms.products_api.modules.product.dto.ProductRequest;
import com.igor.microcomms.products_api.modules.product.dto.ProductResponse;
import com.igor.microcomms.products_api.modules.product.dto.ProductStockDTO;
import com.igor.microcomms.products_api.modules.product.model.Product;
import com.igor.microcomms.products_api.modules.product.repository.ProductRepository;
import com.igor.microcomms.products_api.modules.sales.dto.SalesConfirmationDTO;
import com.igor.microcomms.products_api.modules.sales.enums.SalesStatus;
import com.igor.microcomms.products_api.modules.sales.rabbitmq.SalesConfirmationSender;
import com.igor.microcomms.products_api.modules.supplier.service.SupplierService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import static com.igor.microcomms.products_api.config.util.ResponseUtil.convertToResponse;
import static com.igor.microcomms.products_api.config.util.ResponseUtil.validateNotEmpty;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final SalesConfirmationSender salesConfirmationSender;

    public List<ProductResponse> findAll() {
        return convertToResponse(productRepository.findAll(), ProductResponse::of);
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
        return convertToResponse(productRepository.findByNameIgnoreCaseContaining(name),
                ProductResponse::of);
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId) {
        validateNotEmpty(supplierId, "Supplier ID is required");
        return convertToResponse(productRepository.findBySupplierId(supplierId), ProductResponse::of);
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId) {
        validateNotEmpty(categoryId, "Category ID is required");
        return convertToResponse(productRepository.findByCategoryId(categoryId), ProductResponse::of);
    }

    private void validateProductRequest(ProductRequest request) {
        validateNotEmpty(request.getName(), "Product name was not provided");
        validateNotEmpty(request.getQuantityAvailable(), "Product's available quantity was not provided");
        if (request.getQuantityAvailable() <= 0) {
            throw new ValidationException("Product's available quantity must be greater than or equal to 0");
        }
        validateNotEmpty(request.getCategoryId(), "Category ID was not provided");
        validateNotEmpty(request.getSupplierId(), "Supplier ID was not provided");
    }

    // convert productRequest to Product and save it
    public ProductResponse save(ProductRequest request) {
        validateProductRequest(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }

    public ProductResponse update(Integer id, ProductRequest request) {
        validateNotEmpty(id, "Product ID is required");
        validateProductRequest(request);

        // fetch related entities
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());

        // update product
        var product = Product.of(request, supplier, category);
        product.setId(id);
        productRepository.save(product);

        // fetch timestamps
        var createdAt = productRepository.findCreatedAtById(id);
        var lastModified = productRepository.findLastModifiedById(id);

        // build response
        var response = ProductResponse.of(product);
        response.setCreatedAt(createdAt);
        response.setLastModified(lastModified);

        return response;
    }

    public Boolean existsBySupplierId(Integer supplierId) {
        validateNotEmpty(supplierId, "Supplier ID is required");
        return productRepository.existsBySupplierId(supplierId);
    }

    public Boolean existsByCategoryId(Integer categoryId) {
        validateNotEmpty(categoryId, "Category ID is required");
        return productRepository.existsByCategoryId(categoryId);
    }

    public SuccessResponse delete(Integer id) {
        validateNotEmpty(id, "Product ID is required");
        productRepository.deleteById(id);
        return SuccessResponse.create("Product deleted successfully");
    }

    private void validateStockUpdate(ProductStockDTO product) {
        validateNotEmpty(product, "Product data is required");
        validateNotEmpty(product.getSalesId(), "Sales ID is required");
        validateNotEmpty(product.getProducts(), "Product list is required");
        product.getProducts().forEach(p -> {
            validateNotEmpty(p.getProductId(), "Product ID is required");
            validateNotEmpty(p.getQuantity(), "Product quantity is required");
        });
    }

    private void validateQuantityInStock(ProductQuantityDTO salesProduct, Product existingProduct) {
        var isStockAvailable = existingProduct.getQuantityAvailable() >= salesProduct.getQuantity();
        var exceptionMessage = String.format("Product %s has insufficient stock", existingProduct.getName());
        if (!isStockAvailable)
            throw new ValidationException(exceptionMessage);
    }

    private void updateStock(ProductStockDTO product) {
        product.getProducts().forEach(salesProduct -> {
            var existingProduct = findById(salesProduct.getProductId());
            validateQuantityInStock(salesProduct, existingProduct);
            existingProduct.updateStock(salesProduct.getQuantity());
            productRepository.save(existingProduct);
            var approvedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.APPROVED);
            salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
        });
    }

    public void updateProductStock(ProductStockDTO product) {
        try {
            validateStockUpdate(product);
            updateStock(product);
        } catch (Exception e) {
            log.error("Error updating product stock: {}", e.getMessage(), e);
            var rejectedMessage = new SalesConfirmationDTO(product.getSalesId(), SalesStatus.REJECTED);
            salesConfirmationSender.sendSalesConfirmationMessage(rejectedMessage);

        }
    }

}
