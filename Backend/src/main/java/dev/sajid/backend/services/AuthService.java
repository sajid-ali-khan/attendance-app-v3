package dev.sajid.backend.services;

import dev.sajid.backend.dtos.JwtResponse;
import dev.sajid.backend.dtos.LoginRequest;
import dev.sajid.backend.jwt.JwtUtils;
import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.models.normalized.faculty.FacultyRole;
import dev.sajid.backend.repositories.FacultyRepository;
import org.apache.catalina.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    public Optional<JwtResponse> loginUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Faculty userDetails = (Faculty) authentication.getPrincipal();
        String jwt = jwtUtils.generateTokenFromUsername(userDetails);

        return Optional.of(new JwtResponse(
                jwt,
                "Bearer",
                userDetails.getUsername(),
                userDetails.getRole().name()
        ));
    }
}
