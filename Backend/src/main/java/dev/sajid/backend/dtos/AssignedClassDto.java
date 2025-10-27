package dev.sajid.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignedClassDto {
    int classId; // courseId from backend perspective, classId from client perspective
    String className;
    String subjectName;
}
