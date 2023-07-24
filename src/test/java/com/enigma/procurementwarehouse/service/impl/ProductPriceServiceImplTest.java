package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Category;
import com.enigma.procurementwarehouse.entity.Product;
import com.enigma.procurementwarehouse.entity.ProductPrice;
import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.model.request.ProductRequest;
import com.enigma.procurementwarehouse.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductPriceServiceImplTest {

    @Mock
    private ProductRepository productRepository;



    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest productRequest;
    private Product product;
    private Vendor vendor;
    private ProductPrice productPrice;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest();
        productRequest.setProductName("Test Product");
        productRequest.setVendorId("vendorId");
        productRequest.setCategoryId("categoryId");
        productRequest.setPrice(10000L);
        productRequest.setStock(10);

        product = new Product();
        product.setId("productId");
        product.setName("Test Product");
        product.setCategory(new Category("categoryId", "Test Category"));

        vendor = new Vendor();
        vendor.setId("vendorId");
        vendor.setName("Test Vendor");
        vendor.setAddress("Test Address");
        vendor.setPhone("123456789");

        productPrice = new ProductPrice();
        productPrice.setPrice(10000L);
        productPrice.setStock(10);
        productPrice.setVendor(vendor);
        productPrice.setProduct(product);
        productPrice.setIsActive(true);
    }


    @Test
    void testSoftDeleteProduct() {
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(java.util.Optional.of(product));
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);

        productService.softDelete("productId");

        assertEquals(true, product.getIsDeleted());

        verify(productRepository, times(1)).findByIdAndIsDeletedFalse(anyString());
        verify(productRepository, times(1)).saveAndFlush(any(Product.class));
    }

}
