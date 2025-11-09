package dev.sajid.backend.dtos;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

public record SessionRegisterDto(
        int sessionId,
        String sessionName,
        int presentCount,
        int totalCount,
        Instant updatedAt,
        Map<Integer, AttendanceRecordDto> attendanceRowMap
) {

}
