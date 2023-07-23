package com.enigma.procurementwarehouse.model.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {
    private String id;
    private String productName;
    private String category;
    private Long price;
    private Integer stock;
    private VendorResponse vendorResponse;

}
