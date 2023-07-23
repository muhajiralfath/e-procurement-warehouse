package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Category;
import com.enigma.procurementwarehouse.entity.Product;
import com.enigma.procurementwarehouse.entity.ProductPrice;
import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.model.request.ProductRequest;
import com.enigma.procurementwarehouse.model.response.ProductResponse;
import com.enigma.procurementwarehouse.model.response.VendorResponse;
import com.enigma.procurementwarehouse.repository.ProductRepository;
import com.enigma.procurementwarehouse.service.*;
import com.enigma.procurementwarehouse.specification.ProductSpecification;
import com.enigma.procurementwarehouse.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final VendorService vendorService;
    private final ProductPriceService productPriceService;
    private final ValidationUtil validationUtil;
    private final CategoryService categoryService;
    private final ProductCodeGeneratorService productCodeGeneratorService;
    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse create(ProductRequest request) {
        validationUtil.validate(request);
        Vendor vendor = vendorService.getById(request.getVendorId());
        Category category = categoryService.findById(request.getCategoryId());

        String productCode = productCodeGeneratorService.productCodeGenerator();

        Product product = Product.builder()
                .name(request.getProductName())
                .productCode(productCode)
                .category(category)
                .isDeleted(false)
                .build();
        productRepository.saveAndFlush(product);

        ProductPrice productPrice = ProductPrice.builder()
                .price(request.getPrice())
                .stock(request.getStock())
                .product(product)
                .vendor(vendor)
                .isActive(true)
                .build();
        productPriceService.create(productPrice);

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getName())
                .category(product.getCategory().getName())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .vendorResponse(VendorResponse.builder()
                        .id(vendor.getId())
                        .name(vendor.getName())
                        .address(vendor.getAddress())
                        .phone(vendor.getPhone())
                        .build())
                .build();
    }

    @Override
    public Product getProductById(String id) {
       return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public List<ProductResponse> createBulk(List<ProductRequest> productRequests) {
        try {
        return productRequests.stream().map(this::create).collect(Collectors.toList());
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "products not valid");
        }
    }


    @Override
    public ProductResponse getById(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
        Optional<ProductPrice> productPrice = product.getProductPrices().stream().filter(ProductPrice::getIsActive).findFirst();

        if (productPrice.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        Vendor vendor = productPrice.get().getVendor();

        return toProductResponse(product, productPrice.get(), vendor);
    }

    @Override
    public Page<ProductResponse> getAllByNameOrPrice(String name, Long maxPrice, Integer page, Integer size) {
        Specification<Product> specification = ProductSpecification.getSpecification(name, maxPrice);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(specification, pageable);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products.getContent()) {
            Optional<ProductPrice> productPrice = product.getProductPrices()
                    .stream()
                    .filter(ProductPrice::getIsActive).findFirst();

            if (productPrice.isEmpty()) continue;
            Vendor store = productPrice.get().getVendor();

            productResponses.add(toProductResponse(product, productPrice.get(), store));
        }
        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse update(ProductRequest request) {
        Product currentProduct = findByIdAndDeleteFalseOrThrowNotFound(request.getProductId());
        currentProduct.setName(request.getProductName());

        ProductPrice productPriceActive = productPriceService.findProductPriceActive(request.getProductId(), true);

        if (!productPriceActive.getVendor().getId().equals(request.getVendorId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "store can't be change");
        // TODO: If price different create new product price
        if (!request.getPrice().equals(productPriceActive.getPrice())) {
            productPriceActive.setIsActive(false);
            ProductPrice productPrice = productPriceService.create(ProductPrice.builder()
                    .price(request.getPrice())
                    .stock(request.getStock())
                    .product(currentProduct)
                    .vendor(productPriceActive.getVendor())
                    .isActive(true)
                    .build());
            currentProduct.addProductPrice(productPrice);
            return toProductResponse(currentProduct, productPrice, productPrice.getVendor());
        }
        productPriceActive.setStock(request.getStock());

        return toProductResponse(currentProduct, productPriceActive, productPriceActive.getVendor());
    }

    @Override
    public void softDelete(String id) {
        Product product = findByIdAndDeleteFalseOrThrowNotFound(id);
        product.setIsDeleted(true);
        productRepository.saveAndFlush(product);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void hardDelete(String id) {
        Product product = findByIdAndDeleteFalseOrThrowNotFound(id);
        ProductPrice productPrice = productPriceService.getById(product.getId());
        productPriceService.hardDelete(productPrice.getId());
        productRepository.deleteById(id);
    }

    private Product findByIdAndDeleteFalseOrThrowNotFound(String id) {
        return productRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }

    private ProductResponse toProductResponse(Product product, ProductPrice productPrice, Vendor vendor) {
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getName())
                .category(product.getCategory().getName())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .vendorResponse(VendorResponse.builder()
                        .id(vendor.getId())
                        .name(vendor.getName())
                        .address(vendor.getAddress())
                        .phone(vendor.getPhone())
                        .build())
                .build();
    }
}
