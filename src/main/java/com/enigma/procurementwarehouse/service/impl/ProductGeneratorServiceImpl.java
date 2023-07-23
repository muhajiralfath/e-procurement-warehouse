package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Product;
import com.enigma.procurementwarehouse.repository.ProductRepository;
import com.enigma.procurementwarehouse.service.ProductCodeGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductGeneratorServiceImpl implements ProductCodeGeneratorService {
    private final ProductRepository productRepository;

    @Override
    public String productCodeGenerator() {
// Gunakan Optional untuk menghindari NPE
        String lastProductCode = productRepository.findTopByOrderByProductCodeDesc()
                .map(Product::getProductCode) // Map product menjadi productCode
                .orElse(null); // Jika tidak ada hasil query, set lastProductCode menjadi null

        if (lastProductCode != null) {
            int sequence = Integer.parseInt(lastProductCode.substring(2)) + 1;
            return "KB" + String.format("%02d", sequence);
        } else {
            return "KB01";
        }

    }

}

