package com.enigma.procurementwarehouse.controller;

import com.enigma.procurementwarehouse.model.request.ProductRequest;
import com.enigma.procurementwarehouse.model.response.CommonResponse;
import com.enigma.procurementwarehouse.model.response.PagingResponse;
import com.enigma.procurementwarehouse.model.response.ProductResponse;
import com.enigma.procurementwarehouse.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createNewProduct(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new product")
                        .data(productResponse)
                        .build());
    }

    @PostMapping(path = "/bulk")
    public ResponseEntity<?> createBulkProduct(@RequestBody List<ProductRequest> products) {
        List<ProductResponse> productResponses = productService.createBulk(products);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<List<ProductResponse>>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create bulk product")
                        .data(productResponses)
                        .build());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProductById(Authentication authentication, @PathVariable String id) {
        ProductResponse productResponse = productService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get product")
                        .data(productResponse)
                        .build());
    }
    @GetMapping
    public ResponseEntity<?> getAllProduct(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        Page<ProductResponse> productResponses = productService.getAllByNameOrPrice(name, maxPrice, page - 1, size);
        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(productResponses.getTotalPages())
                .size(size)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all product")
                        .data(productResponses.getContent())
                        .paging(pagingResponse)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.update(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully update product")
                        .data(productResponse)
                        .build());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> softDeleteById(@PathVariable(name = "id") String id) {
        productService.softDelete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete product")
                        .build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> hardDeleteById(@PathVariable(name = "id") String id) {
        productService.hardDelete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully hard delete product")
                        .build());
    }
}
