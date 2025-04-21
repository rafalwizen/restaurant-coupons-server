package com.rafalwizen.restaurantcouponsserver.controller;

import com.rafalwizen.restaurantcouponsserver.dto.ApiResponse;
import com.rafalwizen.restaurantcouponsserver.dto.LoginRequestDto;
import com.rafalwizen.restaurantcouponsserver.dto.LoginResponseDto;
import com.rafalwizen.restaurantcouponsserver.model.Admin;
import com.rafalwizen.restaurantcouponsserver.security.jwt.JwtTokenProvider;
import com.rafalwizen.restaurantcouponsserver.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AdminService adminService;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          AdminService adminService,
                          JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.adminService = adminService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        Admin admin = adminService.findByUsername(loginRequest.getUsername());

        LoginResponseDto responseDto = new LoginResponseDto(jwt, admin.getUsername(), admin.getRole());

        return ResponseEntity.ok(ApiResponse.success("Login successful", responseDto));
    }
}