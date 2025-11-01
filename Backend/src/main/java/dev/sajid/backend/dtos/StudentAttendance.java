package dev.sajid.backend.dtos;

import lombok.Data;

@Data
public class StudentAttendance {
    String roll;
    String name;
    public int totalDays;
    public int presentDays;
    double attendancePercentage;
}
