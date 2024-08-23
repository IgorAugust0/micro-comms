package com.igor.microcomms.products_api.modules.product.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.igor.microcomms.products_api.modules.category.dto.CategoryResponse;
import com.igor.microcomms.products_api.modules.product.model.Product;
import com.igor.microcomms.products_api.modules.supplier.dto.SupplierResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesResponse {

    private Integer id;
    private String name;

    @JsonProperty("quantity_available")
    private Integer quantityAvailable;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty("last_modified")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime lastModified;

    private SupplierResponse supplier;
    private CategoryResponse category;

    private List<String> sales;

    public static ProductSalesResponse of(Product product, List<String> sales) {
        return ProductSalesResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .quantityAvailable(product.getQuantityAvailable())
                .createdAt(product.getCreatedAt())
                .lastModified(product.getLastModified())
                .supplier(SupplierResponse.of(product.getSupplier()))
                .category(CategoryResponse.of(product.getCategory()))
                .sales(sales)
                .build();
    }
}
