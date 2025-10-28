package dev.sajid.backend.dtos;

public record AttendanceRecordDto(
        int id,
        String roll,
        String name,
        boolean status
) {
}
