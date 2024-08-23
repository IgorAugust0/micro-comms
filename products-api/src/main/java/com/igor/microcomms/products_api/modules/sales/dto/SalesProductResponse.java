package com.igor.microcomms.products_api.modules.sales.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesProductResponse {

    private List<String> salesIds;
}
