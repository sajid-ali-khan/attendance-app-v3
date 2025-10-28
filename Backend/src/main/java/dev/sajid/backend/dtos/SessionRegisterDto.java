package dev.sajid.backend.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

public record SessionRegisterDto(
        int sessionId,
        String sessionName,
        int presentCount,
        int totalCount,
        LocalDateTime updatedAt,
        Map<Integer, AttendanceRecordDto> attendanceRowMap
) {

}
