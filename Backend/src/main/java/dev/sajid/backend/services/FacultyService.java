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
                    String subjectFullName = ca.getCourse().getBranchSubject().getSubject().getFullForm();
                    String subjectCode = ca.getCourse().getBranchSubject().getSubject().getShortForm();

                    String subjectName = subjectCode + " - " + subjectFullName;

                    String branchName = ca.getCourse().getBranchSubject().getBranch().getShortForm();
                    int semester = ca.getCourse().getStudentBatch().getSemester();
                    String section = ca.getCourse().getStudentBatch().getSection();

                    String className = String.format(
                            "%s Sem %s - %s",
                            formatedSemester(semester),
                            branchName,
                            section
                    );
                    return new AssignedClassDto(ca.getCourse().getId(), className, subjectName);
                })
                .distinct()
                .toList();
    }

    public String formatedSemester(int sem){
        return sem + switch(sem) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}
