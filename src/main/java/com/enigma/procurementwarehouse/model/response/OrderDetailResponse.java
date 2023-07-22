package com.enigma.procurementwarehouse.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderDetailResponse {
    private String orderDetailId;
    private ProductResponse productResponse;
    private Integer quantity;
    private Long totalPrice;
}
