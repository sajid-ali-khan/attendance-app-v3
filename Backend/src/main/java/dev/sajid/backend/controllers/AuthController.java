package dev.sajid.backend.controllers;

import dev.sajid.backend.dtos.JwtResponse;
import dev.sajid.backend.dtos.LoginRequest;
import dev.sajid.backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Optional<JwtResponse> jwtResponse = authService.loginUser(loginRequest);

        if (jwtResponse.isEmpty()){
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(jwtResponse.get());
    }
}
