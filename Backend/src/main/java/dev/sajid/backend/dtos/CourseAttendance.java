package dev.sajid.backend.dtos;

import lombok.Data;

@Data
public class CourseAttendance {
    Integer courseId;
    String subjectName;
    public int daysPresent;
    public int totalDays;
    double percentage;
}
