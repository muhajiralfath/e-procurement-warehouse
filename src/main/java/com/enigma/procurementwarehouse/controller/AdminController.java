package com.enigma.procurementwarehouse.controller;

import com.enigma.procurementwarehouse.model.response.CommonResponse;
import com.enigma.procurementwarehouse.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/admins")
public class AdminController {
    private final AdminService adminService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteAdminById(@PathVariable(name = "id") String id){
        adminService.deleteAdmin(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete admin")
                        .build()
                );
    }
}
