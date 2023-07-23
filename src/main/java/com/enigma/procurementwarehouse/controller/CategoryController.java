package com.enigma.procurementwarehouse.controller;

import com.enigma.procurementwarehouse.entity.Category;
import com.enigma.procurementwarehouse.model.response.CommonResponse;
import com.enigma.procurementwarehouse.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping
    public ResponseEntity<?> createVendor(@RequestBody Category category){
        categoryService.create(category);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully create category")
                        .data(category)
                        .build()
                );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deletebyId(@PathVariable(name = "id") String id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete category")
                        .build()
                );
    }


}
