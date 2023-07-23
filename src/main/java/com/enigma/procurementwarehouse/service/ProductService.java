package com.enigma.procurementwarehouse.service;

import com.enigma.procurementwarehouse.entity.Product;
import com.enigma.procurementwarehouse.model.request.ProductRequest;
import com.enigma.procurementwarehouse.model.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);

    Product getProductById(String id);
    List<ProductResponse> createBulk(List<ProductRequest> productRequests);
    ProductResponse getById(String id );
    Page<ProductResponse> getAllByNameOrPrice(String name, Long maxPrice, Integer page, Integer size);

    ProductResponse update(ProductRequest request);

    void softDelete(String id);
    void hardDelete(String id);



}
