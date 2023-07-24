package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Product;
import com.enigma.procurementwarehouse.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductGeneratorServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductGeneratorServiceImpl productCodeGeneratorService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void productCodeGeneratorShouldReturnNextCode() {
        String lastProductCode = "KB10";
        when(productRepository.findTopByOrderByProductCodeDesc()).thenReturn(Optional.of(Product.builder().productCode(lastProductCode).build()));

        String generatedCode = productCodeGeneratorService.productCodeGenerator();

        assertEquals("KB11", generatedCode);
    }

    @Test
    void productCodeGeneratorShouldReturnDefaultCode() {
        when(productRepository.findTopByOrderByProductCodeDesc()).thenReturn(Optional.empty());

        String generatedCode = productCodeGeneratorService.productCodeGenerator();

        assertEquals("KB01", generatedCode);
    }
}
