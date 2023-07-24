package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.SuperAdmin;
import com.enigma.procurementwarehouse.repository.SuperAdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuperAdminServiceImplTest {

    @Mock
    private SuperAdminRepository superAdminRepository;

    @InjectMocks
    private SuperAdminServiceImpl superAdminService;

    private SuperAdmin superAdmin;

    @BeforeEach
    void setUp() {
        superAdmin = SuperAdmin.builder()
                .id("1")
                .email("superadmin@example.com")
                .build();
    }

    @Test
    void create_Success() {
        when(superAdminRepository.save(any(SuperAdmin.class))).thenReturn(superAdmin);

        SuperAdmin createdSuperAdmin = superAdminService.create(superAdmin);

        assertNotNull(createdSuperAdmin);
        assertEquals(superAdmin.getEmail(), createdSuperAdmin.getEmail());

        verify(superAdminRepository, times(1)).save(any(SuperAdmin.class));
    }

    @Test
    void create_SuperAdminDataAlreadyUsed_ThrowsResponseStatusException() {
        when(superAdminRepository.save(any(SuperAdmin.class)))
                .thenThrow(new DataIntegrityViolationException("username already exist"));

        assertThrows(ResponseStatusException.class, () -> superAdminService.create(superAdmin));

        verify(superAdminRepository, times(1)).save(any(SuperAdmin.class));
    }
}
