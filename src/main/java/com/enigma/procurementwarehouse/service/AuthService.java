package com.enigma.procurementwarehouse.service;

import com.enigma.procurementwarehouse.model.request.AuthRequest;
import com.enigma.procurementwarehouse.model.response.LoginResponse;
import com.enigma.procurementwarehouse.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerVendor(AuthRequest request);
    RegisterResponse registerAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
