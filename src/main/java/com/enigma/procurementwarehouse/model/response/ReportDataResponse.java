package com.enigma.procurementwarehouse.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ReportDataResponse {
    private String id;
    private String productCode;

    private String date;

    private String vendorName;

    private String productName;

    private String category;

    private Long price;

    private Integer quantity;

    private Long totalPrice;


}
