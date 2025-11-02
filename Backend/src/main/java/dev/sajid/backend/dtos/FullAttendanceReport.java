package dev.sajid.backend.dtos;

import lombok.Data;

import java.util.Map;

@Data
public class FullAttendanceReport {
    String className;
    Map<String, FullStudentAttendance> fullStudentAttendanceMap;
}
