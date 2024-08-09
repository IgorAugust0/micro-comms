package com.igor.microcomms.products_api.modules.supplier.service;

import com.igor.microcomms.products_api.config.exception.ValidationException;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierRequest;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierResponse;
import com.igor.microcomms.products_api.modules.supplier.model.Supplier;
import com.igor.microcomms.products_api.modules.supplier.repository.SupplierRepository;

import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<SupplierResponse> findAll() {
        return convertToResponse(supplierRepository.findAll());
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
        return convertToResponse(supplierRepository.findByNameIgnoreCaseContaining(name));
    }

    private void validateSupplierRequest(SupplierRequest request) {
        validateNotEmpty(request.getName(), "Name is required");
    }

    private void validateNotEmpty(Object value, String message) {
        if (isEmpty(value)) {
            throw new ValidationException(message);
        }
    }

    private List<SupplierResponse> convertToResponse(List<Supplier> suppliers) {
        return suppliers.stream().map(SupplierResponse::of).toList();
    }

    public SupplierResponse save(SupplierRequest request) {
        validateSupplierRequest(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }
}
