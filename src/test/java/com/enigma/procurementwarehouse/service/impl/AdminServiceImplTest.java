package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Admin;
import com.enigma.procurementwarehouse.repository.AdminRepository;
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
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId("admin_id");
        admin.setEmail("admin_username");
        admin.setName("admin_username");
        // Set other properties if needed.
    }

    @Test
    void create_shouldReturnAdmin_whenValidAdminIsGiven() {
        // Arrange
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        // Act
        Admin createdAdmin = adminService.create(admin);

        // Assert
        assertNotNull(createdAdmin);
        assertEquals(admin.getId(), createdAdmin.getId());
        assertEquals(admin.getEmail(), createdAdmin.getEmail());
        // Add more assertions for other properties if needed.
        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    void create_shouldThrowConflictException_whenAdminWithExistingUsernameIsGiven() {
        // Arrange
        when(adminRepository.save(any(Admin.class))).thenThrow(DataIntegrityViolationException.class);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> adminService.create(admin));
    }

    @Test
    void deleteAdmin_shouldCallRepositoryDeleteById_whenValidIdIsGiven() {
        // Arrange
        when(adminRepository.findById(anyString())).thenReturn(java.util.Optional.of(admin));

        // Act
        adminService.deleteAdmin("admin_id");

        // Assert
        verify(adminRepository, times(1)).findById("admin_id");
        verify(adminRepository, times(1)).deleteById("admin_id");
    }


}
