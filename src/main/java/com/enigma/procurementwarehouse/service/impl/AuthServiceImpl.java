package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.*;
import com.enigma.procurementwarehouse.entity.constant.ERole;
import com.enigma.procurementwarehouse.model.request.AuthRequest;
import com.enigma.procurementwarehouse.model.response.LoginResponse;
import com.enigma.procurementwarehouse.model.response.RegisterResponse;
import com.enigma.procurementwarehouse.repository.UserCredentialRepository;
import com.enigma.procurementwarehouse.security.BCryptUtils;
import com.enigma.procurementwarehouse.security.JwtUtils;
import com.enigma.procurementwarehouse.service.AdminService;
import com.enigma.procurementwarehouse.service.AuthService;
import com.enigma.procurementwarehouse.service.RoleService;
import com.enigma.procurementwarehouse.service.SuperAdminService;
import com.enigma.procurementwarehouse.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptUtils bCryptUtils;
    private final RoleService roleService;
    private final AdminService adminService;
    private final JwtUtils jwtUtils;
    private final ValidationUtil validationUtil;
    private final SuperAdminService superAdminService;


    @Transactional
    @Override
    public RegisterResponse registerAdmin(AuthRequest request) {
        try {
            Role role = roleService.getOrSave(ERole.ROLE_ADMIN);
            UserCredential credential = UserCredential.builder()
                    .email(request.getEmail())
                    .password(bCryptUtils.hashPassword(request.getPassword()))
                    .roles(List.of(role))
                    .build();
            userCredentialRepository.saveAndFlush(credential);

            String adminName = request.getEmail().split("@")[0];

            Admin admin = Admin.builder()
                    .email(request.getEmail())
                    .name(adminName)
                    .userCredential(credential)
                    .build();
            adminService.create(admin);
            return RegisterResponse.builder().email(credential.getEmail()).build();
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already exist");
        }
    }

    @Override
    public RegisterResponse registerSuperAdmin(AuthRequest request) {
        try {
            Role role = roleService.getOrSave(ERole.ROLE_SUPER_ADMIN);
            UserCredential credential = UserCredential.builder()
                    .email(request.getEmail())
                    .password(bCryptUtils.hashPassword(request.getPassword()))
                    .roles(List.of(role))
                    .build();
            userCredentialRepository.saveAndFlush(credential);

            SuperAdmin admin = SuperAdmin.builder()
                    .email(request.getEmail())
                    .userCredential(credential)
                    .build();
            superAdminService.create(admin);
            return RegisterResponse.builder().email(credential.getEmail()).build();
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already exist");
        }
    }

    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String token = jwtUtils.generateToken(userDetails.getEmail());

        return LoginResponse.builder()
                .email(userDetails.getEmail())
                .roles(roles)
                .token(token)
                .build();
    }
}
