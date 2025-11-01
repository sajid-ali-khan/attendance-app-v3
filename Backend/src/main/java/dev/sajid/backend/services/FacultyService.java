package dev.sajid.backend.services;

import dev.sajid.backend.dtos.AssignedClassDto;
import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import dev.sajid.backend.repositories.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {
    @Autowired
    private FacultyRepository facultyRepository;

    public List<AssignedClassDto> getAssignedClasses(String facultyId){
        List<CourseAssignment> courseAssignments = facultyRepository.findCourseAssignmentsById(facultyId);

        return courseAssignments.stream()
                .map(ca -> {
                    String className = ClassNamingService.formClassName(ca.getCourse());
                    String subjectName = ClassNamingService.formSubjectName(ca.getCourse());
                    return new AssignedClassDto(ca.getCourse().getId(), className, subjectName);
                })
                .distinct()
                .toList();
    }
}
