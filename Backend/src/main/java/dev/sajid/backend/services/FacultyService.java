package dev.sajid.backend.services;

import dev.sajid.backend.dtos.AssignedClassDto;
import dev.sajid.backend.dtos.ChangePasswordRequest;
import dev.sajid.backend.exceptions.ResourceNotFoundException;
import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.repositories.FacultyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<AssignedClassDto> getAssignedClasses(String facultyId){
        List<CourseAssignment> courseAssignments = facultyRepository.findCourseAssignmentsById(facultyId);

        return courseAssignments.stream()
                .map(ca -> {
                    String className = ClassNamingService.formClassName(ca.getCourse());
                    String subjectShortForm = ca.getCourse().getBranchSubject().getSubject().getShortForm();
                    String subjectFullForm = ca.getCourse().getBranchSubject().getSubject().getFullForm();
                    return new AssignedClassDto(ca.getCourse().getId(), className, subjectShortForm, subjectFullForm);
                })
                .distinct()
                .toList();
    }

    public boolean changePassword(String facultyCode, ChangePasswordRequest changePasswordRequest){
        Faculty faculty = facultyRepository.findByUsername(facultyCode).get();

        if (passwordEncoder.matches(changePasswordRequest.currentPassword(), faculty.getPasswordHash())){
            faculty.setPasswordHash(passwordEncoder.encode(changePasswordRequest.newPassword()));
            facultyRepository.save(faculty);
            return true;
        }else{
            return false;
        }
    }
}
