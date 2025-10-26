package dev.sajid.backend.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.sajid.backend.dtos.ClassAssignmentDto;
import dev.sajid.backend.models.normalized.course.BranchSubject;
import dev.sajid.backend.models.normalized.course.SubjectType;
import dev.sajid.backend.models.normalized.derived.Course;
import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.repositories.*;
import org.springframework.stereotype.Service;

import dev.sajid.backend.dtos.FacultyDto;
import dev.sajid.backend.dtos.ViewAssignmentDto;

@Service
public class CourseAssignmentService {
    private final CourseAssignmentRepository courseAssignmentRepository;
    private final StudentBatchRepository studentBatchRepository;
    private final BranchSubjectRepository branchSubjectRepository;
    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;

    public CourseAssignmentService(
            CourseAssignmentRepository courseAssignmentRepository,
            StudentBatchRepository studentBatchRepository,
            BranchSubjectRepository branchSubjectRepository,
            CourseRepository courseRepository,
            FacultyRepository facultyRepository
            ) {
        this.courseAssignmentRepository = courseAssignmentRepository;
        this.studentBatchRepository = studentBatchRepository;
        this.branchSubjectRepository = branchSubjectRepository;
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
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

    public Optional<CourseAssignment> createCourseAssignment(ClassAssignmentDto classAssignmentDto){
        // first create the course object
        // ------------------------------------------------
        // 1. fetch the student batch using branchId, semester, section
        StudentBatch studentBatch = studentBatchRepository.findFirstByBranchIdAndSemesterAndSection(
                classAssignmentDto.branchId(),
                classAssignmentDto.semester(),
                classAssignmentDto.section()).orElseThrow(() -> new RuntimeException("Student batch not found"));
        // 2. fetch the branch subject using branchId, semester, subjectId
        BranchSubject branchSubject = branchSubjectRepository.findFirstByBranchIdAndSubjectIdAndSemester(
                classAssignmentDto.branchId(),
                classAssignmentDto.subjectId(),
                classAssignmentDto.semester()).orElseThrow(() -> new RuntimeException("Branch subject not found"));
        // 3. check if the course already exists
        if (branchSubject.getSubject().getSubjectType().equals(SubjectType.Theory) && courseRepository.existsByStudentBatchIdAndBranchSubjectId(studentBatch.getId(), branchSubject.getId())){
            throw new RuntimeException("Course already exists, multiple faculties can't be assigned to a single theory subject.");
        }
        Course course = new Course();
        course.setStudentBatch(studentBatch);
        course.setBranchSubject(branchSubject);
        course = courseRepository.save(course);
        // ------------------------------------------------
        // now create the course assignment object
        // ------------------------------------------------
        CourseAssignment courseAssignment = new CourseAssignment();
        courseAssignment.setCourse(course);
        Faculty faculty = facultyRepository.findById(classAssignmentDto.facultyId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        courseAssignment.setFaculty(faculty);
        courseAssignment.setAssignedRole("INSTRUCTOR");

        return Optional.of(courseAssignmentRepository.save(courseAssignment));
    }

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
