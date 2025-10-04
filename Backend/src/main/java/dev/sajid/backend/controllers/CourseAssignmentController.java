package dev.sajid.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sajid.backend.dtos.ClassAssignmentDto;
import dev.sajid.backend.models.normalized.course.BranchSubject;
import dev.sajid.backend.models.normalized.derived.Course;
import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.repositories.BranchSubjectRepository;
import dev.sajid.backend.repositories.CourseAssignmentRepository;
import dev.sajid.backend.repositories.CourseRepository;
import dev.sajid.backend.repositories.FacultyRepository;
import dev.sajid.backend.repositories.StudentBatchRepository;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/course-assignments")
@CrossOrigin
public class CourseAssignmentController {
    private final CourseRepository courseRepository;
    private final CourseAssignmentRepository courseAssignmentRepository;
    private final StudentBatchRepository studentBatchRepository;
    private final BranchSubjectRepository branchSubjectRepository;
    private final FacultyRepository facultyRepository;

    CourseAssignmentController(CourseAssignmentRepository courseAssignmentRepository, CourseRepository courseRepository, StudentBatchRepository studentBatchRepository, BranchSubjectRepository branchSubjectRepository, FacultyRepository facultyRepository) {
        this.courseAssignmentRepository = courseAssignmentRepository;
        this.courseRepository = courseRepository;
        this.studentBatchRepository = studentBatchRepository;
        this.branchSubjectRepository = branchSubjectRepository;
        this.facultyRepository = facultyRepository;
    }

    @GetMapping("/student_batches")
    public ResponseEntity<StudentBatch> getStudentBatch(@RequestParam("branchId") int branchId,
    @RequestParam("semester") int semester,
    @RequestParam("section") String section) {
        return ResponseEntity.ok(studentBatchRepository.findFirstByBranchIdAndSemesterAndSection(branchId, semester, section).orElseThrow(() -> new RuntimeException("BranchSubject not found")));
    }

    @GetMapping("/branch-subjects")
    public ResponseEntity<BranchSubject> getBranchSubjects(@RequestParam("branchId") int branchId,
    @RequestParam("semester") int semester,
    @RequestParam("subjectId") int subjectId) {
        return ResponseEntity.ok(branchSubjectRepository.findFirstByBranchIdAndSubjectIdAndSemester(branchId, subjectId, semester).orElseThrow(() -> new RuntimeException("BranchSubject not found")));
    }
    

    @PostMapping("")
    @Transactional
    public ResponseEntity<CourseAssignment> createCourseAssignment(@RequestBody ClassAssignmentDto classAssignmentDto) {
        // first create the course object
        // ------------------------------------------------
        // 1. fetch the student batch using branchId, semester, section
        StudentBatch studentBatch = studentBatchRepository.findFirstByBranchIdAndSemesterAndSection(
                classAssignmentDto.branchId(),
                classAssignmentDto.semester(),
                classAssignmentDto.section()
        ).orElseThrow(() -> new RuntimeException("Student batch not found"));
        // 2. fetch the branch subject using branchId, semester, subjectId
        BranchSubject branchSubject = branchSubjectRepository.findFirstByBranchIdAndSubjectIdAndSemester(
                classAssignmentDto.branchId(),
                classAssignmentDto.subjectId(),
                classAssignmentDto.semester()
        ).orElseThrow(() -> new RuntimeException("Branch subject not found"));
        // 3. check if the course already exists
        if (courseRepository.existsByStudentBatchIdAndBranchSubjectId(studentBatch.getId(), branchSubject.getId())) {
            throw new RuntimeException("Course already exists");
        }
        // 4. create the course object and save it
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
        courseAssignment = courseAssignmentRepository.save(courseAssignment);
        return ResponseEntity.ok(courseAssignment);
    }
}
