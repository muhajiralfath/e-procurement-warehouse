package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.SuperAdmin;
import com.enigma.procurementwarehouse.repository.SuperAdminRepository;
import com.enigma.procurementwarehouse.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {
    private final SuperAdminRepository superAdminRepository;
    @Override
    public SuperAdmin create(SuperAdmin admin) {
        try {
            return superAdminRepository.save(admin);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exist");
        }
    }
}
