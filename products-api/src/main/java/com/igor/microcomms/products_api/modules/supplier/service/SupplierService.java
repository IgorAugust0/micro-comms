package com.igor.microcomms.products_api.modules.supplier.service;

import com.igor.microcomms.products_api.config.exception.ValidationException;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierRequest;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierResponse;
import com.igor.microcomms.products_api.modules.supplier.model.Supplier;
import com.igor.microcomms.products_api.modules.supplier.repository.SupplierRepository;
import static com.igor.microcomms.products_api.util.ResponseUtil.convertToResponse;
import static com.igor.microcomms.products_api.util.ResponseUtil.validateNotEmpty;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

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
}
