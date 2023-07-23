package com.enigma.procurementwarehouse.service.impl;

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
        String lastProductCode = productRepository.findTopByOrderByProductCodeDesc().getProductCode();

        if (lastProductCode != null) {
            int sequence = Integer.parseInt(lastProductCode.substring(2)) + 1;
            return "KB" + String.format("%02d", sequence);
        } else {
            return "KB01";
        }
    }
}
