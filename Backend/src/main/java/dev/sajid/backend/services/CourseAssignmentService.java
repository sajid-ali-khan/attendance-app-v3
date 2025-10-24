package dev.sajid.backend.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.sajid.backend.dtos.FacultyDto;
import dev.sajid.backend.dtos.ViewAssignmentDto;
import dev.sajid.backend.repositories.CourseAssignmentRepository;

@Service
public class CourseAssignmentService {
    private final CourseAssignmentRepository courseAssignmentRepository;

    public CourseAssignmentService(CourseAssignmentRepository courseAssignmentRepository) {
        this.courseAssignmentRepository = courseAssignmentRepository;
    }

    private record AssignmentDto(
            String subjectCode,
            String subjectName,
            String subjectType,
            String facultyCode,
            String facultyName
    ){}

    private record SubjectDto(
        String subjectCode,
        String subjectName,
        String subjectType
    ){}

    public List<ViewAssignmentDto> viewAssignments(int branchId, int semester, String section) {
        List<AssignmentDto> assignments = courseAssignmentRepository.findAssignments(branchId, semester, section)
                .stream()
                .map(assignment -> new AssignmentDto(
                        assignment.getCourse().getBranchSubject().getSubject().getShortForm(),
                        assignment.getCourse().getBranchSubject().getSubject().getFullForm(),
                        assignment.getCourse().getBranchSubject().getSubject().getSubjectType().toString(),
                        assignment.getFaculty().getCode(),
                        assignment.getFaculty().getName()
                ))
                .collect(Collectors.toList());
        
        Map<SubjectDto, List<FacultyDto>> subjectFacultyMap = new HashMap<>();

        for (AssignmentDto assignment : assignments) {
            SubjectDto subject = new SubjectDto(
                    assignment.subjectCode(),
                    assignment.subjectName(),
                    assignment.subjectType()
            );
            FacultyDto faculty = new FacultyDto(
                    assignment.facultyCode(),
                    assignment.facultyName()
            );

            if (!subjectFacultyMap.containsKey(subject)) {
                subjectFacultyMap.put(subject, new java.util.ArrayList<>());
            }
            subjectFacultyMap.get(subject).add(faculty);
        }

        return subjectFacultyMap.entrySet().stream()
                .map(entry -> new ViewAssignmentDto(
                        entry.getKey().subjectCode(),
                        entry.getKey().subjectName(),
                        entry.getKey().subjectType(),
                        entry.getValue()
                ))
                .collect(Collectors.toList());
    }
}
