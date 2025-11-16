package dev.sajid.backend.dtos;

import lombok.Data;

import java.util.Map;

@Data
public class StudentSubjectsAttendance {
    String roll;
    String name;
    Map<Integer, SubjectAttendance> subjectAttendanceMap;
}
