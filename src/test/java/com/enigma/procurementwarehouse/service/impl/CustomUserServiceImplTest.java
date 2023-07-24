package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Role;
import com.enigma.procurementwarehouse.entity.UserCredential;
import com.enigma.procurementwarehouse.entity.constant.ERole;
import com.enigma.procurementwarehouse.repository.UserCredentialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserServiceImplTest {

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @InjectMocks
    private CustomUserServiceImpl customUserService;

    private UserCredential userCredential;

    @BeforeEach
    void setUp() {
        Role roleAdmin = Role.builder().role(ERole.ROLE_ADMIN).build();
        userCredential = UserCredential.builder()
                .email("test@example.com")
                .password("hashedPassword")
                .roles(Collections.singletonList(roleAdmin))
                .build();
    }

    @Test
    void loadUserByUsername_Success() {
        when(userCredentialRepository.findByEmail(userCredential.getEmail())).thenReturn(Optional.of(userCredential));

        UserDetails userDetails = customUserService.loadUserByUsername(userCredential.getEmail());

        assertNotNull(userDetails);
        assertEquals(userCredential.getEmail(), userDetails.getUsername());
        assertEquals(userCredential.getPassword(), userDetails.getPassword());

        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) userDetails.getAuthorities();
        assertFalse(authorities.isEmpty());
        assertEquals(ERole.ROLE_ADMIN.name(), authorities.get(0).getAuthority());

        verify(userCredentialRepository, times(1)).findByEmail(userCredential.getEmail());
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        when(userCredentialRepository.findByEmail(userCredential.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserService.loadUserByUsername(userCredential.getEmail()));

        verify(userCredentialRepository, times(1)).findByEmail(userCredential.getEmail());
    }
}
