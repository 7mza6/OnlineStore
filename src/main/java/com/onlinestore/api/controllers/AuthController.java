package com.onlinestore.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinestore.api.dto.AuthResponse;
import com.onlinestore.api.dto.LoginRequest;
import com.onlinestore.api.dto.RegisterRequest;
import com.onlinestore.api.entities.Role;
import com.onlinestore.api.entities.User;
import com.onlinestore.security.JwtService;
import com.onlinestore.security.UserPrincipal;
import com.onlinestore.service.interfaces.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request.getName(), request.getEmail(), request.getPassword(),
                Role.CUSTOMER);
        String token = jwtService.generateToken(new UserPrincipal(user));
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userService.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(new UserPrincipal(user));
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getRole().name()));
    }
}
