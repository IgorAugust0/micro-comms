package com.igor.microcomms.products_api.modules.sales.dto;

import com.igor.microcomms.products_api.modules.sales.enums.SalesStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesConfirmationDTO {
    
    private String salesId;
    private SalesStatus status;
}
