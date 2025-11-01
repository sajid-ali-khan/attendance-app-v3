package dev.sajid.backend.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class AttendanceReport {
    String className;
    String subjectName;
    Map<String, StudentAttendance> studentAttendanceMap;
}
