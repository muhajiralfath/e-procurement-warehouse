package com.enigma.procurementwarehouse.model.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderRequest {

    private String customerId;
    private List<OrderDetailRequest> orderDetails;

}
