package com.enigma.procurementwarehouse.service;

import com.enigma.procurementwarehouse.model.request.OrderRequest;
import com.enigma.procurementwarehouse.model.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createNewTransaction(OrderRequest request);
    OrderResponse getOrderById(String id);
    List<OrderResponse> getAllTransaction(String customerName);



}
