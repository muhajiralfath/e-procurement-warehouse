package com.enigma.procurementwarehouse.service;

import com.enigma.procurementwarehouse.entity.ProductPrice;

public interface ProductPriceService {
    ProductPrice create(ProductPrice productPrice);
    ProductPrice getById(String id);
    ProductPrice findProductPriceActive(String productId, Boolean active);
}
