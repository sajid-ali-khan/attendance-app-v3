package dev.sajid.backend.dtos;

import java.time.LocalDateTime;

public record SessionDto (
        ClassDto classDto,
        LocalDateTime createdAt
){
}
