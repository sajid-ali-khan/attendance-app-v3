package dev.sajid.backend.dtos;

import java.time.Instant;

public record SessionDto (
        int sessionId,
        String sessionName,
        Instant updatedAt
){
}
