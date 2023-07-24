package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Category;
import com.enigma.procurementwarehouse.entity.Product;
import com.enigma.procurementwarehouse.entity.ProductPrice;
import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.model.request.ProductRequest;
import com.enigma.procurementwarehouse.repository.ProductRepository;
import com.enigma.procurementwarehouse.service.CategoryService;
import com.enigma.procurementwarehouse.service.ProductCodeGeneratorService;
import com.enigma.procurementwarehouse.service.ProductPriceService;
import com.enigma.procurementwarehouse.service.VendorService;
import com.enigma.procurementwarehouse.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private VendorService vendorService;

    @Mock
    private ProductPriceService productPriceService;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductCodeGeneratorService productCodeGeneratorService;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest productRequest;
    private Vendor vendor;
    private Category category;
    private Product product;
    private ProductPrice productPrice;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .vendorId("1")
                .categoryId("1")
                .productName("Product 1")
                .price((long) 100.0)
                .stock(10)
                .build();

        vendor = Vendor.builder()
                .id("1")
                .name("Vendor 1")
                .address("Address 1")
                .phone("123456789")
                .build();

        category = Category.builder()
                .id("1")
                .name("Category 1")
                .build();

        product = Product.builder()
                .id("1")
                .name("Product 1")
                .category(category)
                .isDeleted(false)
                .build();

        productPrice = ProductPrice.builder()
                .id("1")
                .price((long) 100.0)
                .stock(10)
                .product(product)
                .vendor(vendor)
                .isActive(true)
                .build();
    }


    @Test
    void create_InvalidRequest_ThrowsResponseStatusException() {
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request")).when(validationUtil).validate(any());

        assertThrows(ResponseStatusException.class, () -> productService.create(productRequest));

        verify(validationUtil, times(1)).validate(any());
        verify(vendorService, times(0)).getById(anyString());
        verify(categoryService, times(0)).findById(anyString());
        verify(productCodeGeneratorService, times(0)).productCodeGenerator();
        verify(productRepository, times(0)).saveAndFlush(any(Product.class));
        verify(productPriceService, times(0)).create(any(ProductPrice.class));
    }

    @Test
    void getProductById_ProductFound_ReturnsProduct() {
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));

        Product resultProduct = productService.getProductById("1");

        assertNotNull(resultProduct);
        assertEquals(product, resultProduct);

        verify(productRepository, times(1)).findById(anyString());
    }

    @Test
    void getProductById_ProductNotFound_ThrowsResponseStatusException() {
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productService.getProductById("1"));

        verify(productRepository, times(1)).findById(anyString());
    }



    @Test
    void getById_ProductNotFound_ThrowsResponseStatusException() {
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productService.getById("1"));

        verify(productRepository, times(1)).findById(anyString());
        verify(productPriceService, times(0)).findProductPriceActive(anyString(), anyBoolean());
    }




    @Test
    void update_ProductNotFound_ThrowsResponseStatusException() {
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        ProductRequest updatedProductRequest = ProductRequest.builder()
                .productId("1")
                .vendorId("1")
                .categoryId("1")
                .productName("Product 1 Updated")
                .price((long) 150.0)
                .stock(20)
                .build();

        assertThrows(ResponseStatusException.class, () -> productService.update(updatedProductRequest));

        verify(productRepository, times(1)).findByIdAndIsDeletedFalse(anyString());
        verify(productPriceService, times(0)).findProductPriceActive(anyString(), anyBoolean());
        verify(productPriceService, times(0)).create(any(ProductPrice.class));
    }

    @Test
    void update_VendorIdDoesNotMatch_ThrowsResponseStatusException() {
        productPrice.setIsActive(true);
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(product));
        when(productPriceService.findProductPriceActive(anyString(), anyBoolean())).thenReturn(productPrice);

        ProductRequest updatedProductRequest = ProductRequest.builder()
                .productId("1")
                .vendorId("2") // Different vendor ID
                .categoryId("1")
                .productName("Product 1 Updated")
                .price((long) 150.0)
                .stock(20)
                .build();

        assertThrows(ResponseStatusException.class, () -> productService.update(updatedProductRequest));

        verify(productRepository, times(1)).findByIdAndIsDeletedFalse(anyString());
        verify(productPriceService, times(1)).findProductPriceActive(anyString(), anyBoolean());
        verify(productPriceService, times(0)).create(any(ProductPrice.class));
    }

    @Test
    void softDelete_Success() {
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(product));
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);

        productService.softDelete("1");

        assertTrue(product.getIsDeleted());

        verify(productRepository, times(1)).findByIdAndIsDeletedFalse(anyString());
        verify(productRepository, times(1)).saveAndFlush(any(Product.class));
    }

    @Test
    void softDelete_ProductNotFound_ThrowsResponseStatusException() {
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productService.softDelete("1"));

        verify(productRepository, times(1)).findByIdAndIsDeletedFalse(anyString());
        verify(productRepository, times(0)).saveAndFlush(any(Product.class));
    }

    @Test
    void hardDelete_Success() {
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.of(product));
        when(productPriceService.getById(anyString())).thenReturn(productPrice);
        doNothing().when(productPriceService).hardDelete(anyString());
        doNothing().when(productRepository).deleteById(anyString());

        productService.hardDelete("1");

        verify(productRepository, times(1)).findByIdAndIsDeletedFalse(anyString());
        verify(productPriceService, times(1)).getById(anyString());
        verify(productPriceService, times(1)).hardDelete(anyString());
        verify(productRepository, times(1)).deleteById(anyString());
    }

    @Test
    void hardDelete_ProductNotFound_ThrowsResponseStatusException() {
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productService.hardDelete("1"));

        verify(productRepository, times(1)).findByIdAndIsDeletedFalse(anyString());
        verify(productPriceService, times(0)).getById(anyString());
        verify(productPriceService, times(0)).hardDelete(anyString());
        verify(productRepository, times(0)).deleteById(anyString());
    }
}
