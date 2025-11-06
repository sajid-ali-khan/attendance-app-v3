package dev.sajid.backend.dtos;

import lombok.Data;

@Data
public class SubjectAttendance {
    Integer subjectId;
    String subjectName;
    public int daysPresent;
    public int totalDays;
    double percentage;
}
