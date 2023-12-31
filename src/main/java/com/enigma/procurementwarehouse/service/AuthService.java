package com.enigma.procurementwarehouse.service;

import com.enigma.procurementwarehouse.model.request.AuthRequest;
import com.enigma.procurementwarehouse.model.response.LoginResponse;
import com.enigma.procurementwarehouse.model.response.RegisterResponse;

public interface AuthService {

    RegisterResponse registerAdmin(AuthRequest request);

    RegisterResponse registerSuperAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
