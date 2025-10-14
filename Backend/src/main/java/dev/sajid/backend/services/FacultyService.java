package dev.sajid.backend.services;

import dev.sajid.backend.dtos.ClassDto;
import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import dev.sajid.backend.repositories.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {
    @Autowired
    private FacultyRepository facultyRepository;

    public List<ClassDto> getAssignedClasses(int facultyId){
        List<CourseAssignment> courseAssignments = facultyRepository.findCourseAssignmentsById(facultyId);

        return courseAssignments.stream()
                .map(ca -> {
                    String subjectName = ca.getCourse().getBranchSubject().getSubject().getFullForm();
                    String subjectCode = ca.getCourse().getBranchSubject().getSubject().getShortForm();

                    String subject = subjectCode + " - " + subjectName;

                    String branchName = ca.getCourse().getBranchSubject().getBranch().getShortForm();
                    int semester = ca.getCourse().getStudentBatch().getSemester();
                    String section = ca.getCourse().getStudentBatch().getSection();

                    return new ClassDto(subject, branchName, semester, section);
                })
                .distinct()
                .toList();
    }
}
