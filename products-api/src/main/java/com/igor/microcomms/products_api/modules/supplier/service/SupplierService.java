package com.igor.microcomms.products_api.modules.supplier.service;

import com.igor.microcomms.products_api.config.exception.ValidationException;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierRequest;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierResponse;
import com.igor.microcomms.products_api.modules.supplier.model.Supplier;
import com.igor.microcomms.products_api.modules.supplier.repository.SupplierRepository;

import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier findById(Integer id) {
        return supplierRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Supplier not found"));
    }

    // convert CategoryRequest to Category and save it
    public SupplierResponse save(SupplierRequest request) {
        validateSupplierRequest(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    // validate if the request is not empty
    private void validateSupplierRequest(SupplierRequest request) {
        if (isEmpty(request.getName())) {
            throw new ValidationException("Description is required");
        }
    }
}
