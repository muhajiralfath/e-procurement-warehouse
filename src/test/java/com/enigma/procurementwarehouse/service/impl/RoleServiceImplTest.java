package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Role;
import com.enigma.procurementwarehouse.entity.constant.ERole;
import com.enigma.procurementwarehouse.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        roleAdmin = Role.builder()
                .id("1")
                .role(ERole.ROLE_ADMIN)
                .build();
    }

    @Test
    void getOrSave_RoleExists_ReturnsExistingRole() {
        when(roleRepository.findByRole(ERole.ROLE_ADMIN)).thenReturn(Optional.of(roleAdmin));

        Role resultRole = roleService.getOrSave(ERole.ROLE_ADMIN);

        assertNotNull(resultRole);
        assertEquals(roleAdmin.getId(), resultRole.getId());
        assertEquals(roleAdmin.getRole(), resultRole.getRole());

        verify(roleRepository, times(1)).findByRole(ERole.ROLE_ADMIN);
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void getOrSave_RoleDoesNotExist_CreatesAndReturnsNewRole() {
        when(roleRepository.findByRole(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(roleAdmin);

        Role resultRole = roleService.getOrSave(ERole.ROLE_ADMIN);

        assertNotNull(resultRole);
        assertEquals(roleAdmin.getId(), resultRole.getId());
        assertEquals(roleAdmin.getRole(), resultRole.getRole());

        verify(roleRepository, times(1)).findByRole(ERole.ROLE_ADMIN);
        verify(roleRepository, times(1)).save(any(Role.class));
    }
}
