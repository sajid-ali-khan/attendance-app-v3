package dev.sajid.backend.dtos;

import lombok.Data;

@Data
public class CourseAttendance {
    Integer courseId;
    String className;
    int daysPresent;
    int totalDays;
    double percentage;
}
