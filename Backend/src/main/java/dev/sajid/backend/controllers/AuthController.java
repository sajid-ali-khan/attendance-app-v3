package dev.sajid.backend.controllers;

import dev.sajid.backend.dtos.JwtResponse;
import dev.sajid.backend.dtos.LoginRequest;
import dev.sajid.backend.services.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Optional<JwtResponse> jwtResponse;
        log.debug("User: {} requested login.", loginRequest);
        jwtResponse = authService.loginUser(loginRequest);
        log.info("User {} logged in.", loginRequest.getUsername());
        return ResponseEntity.ok(jwtResponse.get());
    }
}
