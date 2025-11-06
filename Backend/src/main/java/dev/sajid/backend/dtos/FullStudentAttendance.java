package dev.sajid.backend.dtos;

import lombok.Data;

import java.util.Map;

@Data
public class FullStudentAttendance {
    String roll;
    String name;
    Map<Integer, SubjectAttendance> subjectAttendanceMap;
}
