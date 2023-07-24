package com.enigma.procurementwarehouse.controller;

import com.enigma.procurementwarehouse.model.request.AuthRequest;
import com.enigma.procurementwarehouse.model.response.CommonResponse;
import com.enigma.procurementwarehouse.model.response.LoginResponse;
import com.enigma.procurementwarehouse.model.response.RegisterResponse;
import com.enigma.procurementwarehouse.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest request) {
        RegisterResponse register = authService.registerAdmin(request);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully registered admin")
                .data(register)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commonResponse);
    }

    @PostMapping(path = "/register-super-admin")
    public ResponseEntity<?> registerSuperAdmin(@RequestBody AuthRequest request) {
        RegisterResponse register = authService.registerSuperAdmin(request);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully registered super admin")
                .data(register)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commonResponse);
    }
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        LoginResponse response = authService.login(request);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully login")
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(commonResponse);
    }
}
