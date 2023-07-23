package com.enigma.procurementwarehouse.controller;

import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.model.request.UpdateVendoreRequest;
import com.enigma.procurementwarehouse.model.request.VendorRequest;
import com.enigma.procurementwarehouse.model.response.CommonResponse;
import com.enigma.procurementwarehouse.model.response.PagingResponse;
import com.enigma.procurementwarehouse.model.response.VendorResponse;
import com.enigma.procurementwarehouse.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/vendors")
public class VendorController {
    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<?> createVendor(@RequestBody VendorRequest vendor){
        vendorService.create(vendor);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully create vendor")
                        .data(vendor)
                        .build()
                );
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getVendoreById(@PathVariable(name = "id") String id){
        Vendor vendor = vendorService.getById(id);
        VendorResponse vendorResponse = VendorResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .address(vendor.getAddress())
                .phone(vendor.getPhone())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get vendor by id")
                        .data(vendorResponse)
                        .build()
                );
    }

    @GetMapping
    public ResponseEntity<?> getAllVendors(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size){
        Page<VendorResponse> allVendor = vendorService.getAllVendor(page - 1 , size);

        PagingResponse pagingResponse = PagingResponse.builder()
                .currentPage(page)
                .totalPage(allVendor.getTotalPages())
                .size(size)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all vendor")
                        .data(allVendor.getContent())
                        .paging(pagingResponse)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateVendor(@RequestBody UpdateVendoreRequest request){
        VendorResponse vendorResponse = vendorService.update(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully update vendor")
                        .data(vendorResponse)
                        .build()
                );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deletebyId(@PathVariable(name = "id") String id){
        vendorService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete vendor")
                        .build()
                );
    }


}
