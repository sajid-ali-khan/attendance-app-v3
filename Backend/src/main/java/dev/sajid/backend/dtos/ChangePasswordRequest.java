package dev.sajid.backend.dtos;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}
