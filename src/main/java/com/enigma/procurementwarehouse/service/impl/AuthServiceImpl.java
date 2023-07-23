package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.model.request.AuthRequest;
import com.enigma.procurementwarehouse.model.response.LoginResponse;
import com.enigma.procurementwarehouse.model.response.RegisterResponse;
import com.enigma.procurementwarehouse.repository.UserCredentialRepository;
import com.enigma.procurementwarehouse.security.BCryptUtils;
import com.enigma.procurementwarehouse.security.JwtUtils;
import com.enigma.procurementwarehouse.service.AdminService;
import com.enigma.procurementwarehouse.service.AuthService;
import com.enigma.procurementwarehouse.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository credentialRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptUtils bCryptUtils;
    private final RoleService roleService;
    private final AdminService adminService;
    private final JwtUtils jwtUtils;




    @Override
    public RegisterResponse registerAdmin(AuthRequest request) {
        return null;
    }

    @Override
    public LoginResponse login(AuthRequest request) {
        return null;
    }
}
