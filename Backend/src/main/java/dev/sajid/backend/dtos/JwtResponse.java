package dev.sajid.backend.dtos;

import lombok.Data;

import java.util.Objects;

@Data
public final class JwtResponse {
    private final String token;
    private final String type;
    private final String username;
    private final String role;
}
