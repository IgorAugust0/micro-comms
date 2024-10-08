package com.igor.microcomms.products_api.modules.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDTO {

    private String salesId;
    private List<ProductQuantityDTO> products;
    
}
