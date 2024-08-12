package com.igor.microcomms.products_api.modules.supplier.service;

import com.igor.microcomms.products_api.config.exception.SuccessResponse;
import com.igor.microcomms.products_api.config.exception.ValidationException;
import com.igor.microcomms.products_api.modules.product.service.ProductService;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierRequest;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierResponse;
import com.igor.microcomms.products_api.modules.supplier.model.Supplier;
import com.igor.microcomms.products_api.modules.supplier.repository.SupplierRepository;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static com.igor.microcomms.products_api.config.util.ResponseUtil.convertToResponse;
import static com.igor.microcomms.products_api.config.util.ResponseUtil.validateNotEmpty;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = { @Lazy })
public class SupplierService {

    private final SupplierRepository supplierRepository;
    @Lazy
    private final ProductService productService;

    public List<SupplierResponse> findAll() {
        return convertToResponse(supplierRepository.findAll(), SupplierResponse::of);
    }

    public Supplier findById(Integer id) {
        validateNotEmpty(id, "Supplier ID is required");
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Supplier not found"));
    }

    public SupplierResponse findIdResponse(Integer id) {
        return SupplierResponse.of(findById(id));
    }

    public List<SupplierResponse> findByName(String name) {
        validateNotEmpty(name, "Supplier name is required");
        return convertToResponse(supplierRepository.findByNameIgnoreCaseContaining(name),
                SupplierResponse::of);
    }

    private void validateSupplierRequest(SupplierRequest request) {
        validateNotEmpty(request.getName(), "Name is required");
    }

    public SupplierResponse save(SupplierRequest request) {
        validateSupplierRequest(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public SupplierResponse update(Integer id, SupplierRequest request) {
        validateNotEmpty(id, "Supplier ID is required");
        validateSupplierRequest(request);
        var supplier = Supplier.of(request);
        // var supplier = findById(id);
        // supplier.setName(request.getName());
        supplier.setId(id);
        supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    public SuccessResponse delete(Integer id) {
        validateNotEmpty(id, "Supplier ID is required");
        if (productService.existsBySupplierId(id)) {
            throw new ValidationException("Supplier has products associated with it");
        }
        supplierRepository.deleteById(id);
        return SuccessResponse.create("Supplier deleted successfully");
    }
}
