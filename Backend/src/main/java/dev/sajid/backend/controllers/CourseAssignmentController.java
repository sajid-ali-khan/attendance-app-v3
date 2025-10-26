package dev.sajid.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sajid.backend.dtos.ClassAssignmentDto;
import dev.sajid.backend.dtos.ViewAssignmentDto;
import dev.sajid.backend.models.normalized.course.BranchSubject;
import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import dev.sajid.backend.repositories.BranchSubjectRepository;
import dev.sajid.backend.repositories.CourseAssignmentRepository;
import dev.sajid.backend.repositories.CourseRepository;
import dev.sajid.backend.repositories.FacultyRepository;
import dev.sajid.backend.repositories.StudentBatchRepository;
import dev.sajid.backend.services.CourseAssignmentService;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/course-assignments")
@CrossOrigin
public class CourseAssignmentController {
    private final StudentBatchRepository studentBatchRepository;
    private final BranchSubjectRepository branchSubjectRepository;
    private final CourseAssignmentService courseAssignmentService;

    CourseAssignmentController(CourseAssignmentRepository courseAssignmentRepository, CourseRepository courseRepository,
            StudentBatchRepository studentBatchRepository, BranchSubjectRepository branchSubjectRepository,
            FacultyRepository facultyRepository, CourseAssignmentService courseAssignmentService) {
        this.studentBatchRepository = studentBatchRepository;
        this.branchSubjectRepository = branchSubjectRepository;
        this.courseAssignmentService = courseAssignmentService;
    }

    @GetMapping("/student_batches")
    public ResponseEntity<StudentBatch> getStudentBatch(@RequestParam("branchId") int branchId,
            @RequestParam("semester") int semester,
            @RequestParam("section") String section) {
        return ResponseEntity
                .ok(studentBatchRepository.findFirstByBranchIdAndSemesterAndSection(branchId, semester, section)
                        .orElseThrow(() -> new RuntimeException("BranchSubject not found")));
    }

    @GetMapping("/branch-subjects")
    public ResponseEntity<BranchSubject> getBranchSubjects(@RequestParam("branchId") int branchId,
            @RequestParam("semester") int semester,
            @RequestParam("subjectId") int subjectId) {
        return ResponseEntity
                .ok(branchSubjectRepository.findFirstByBranchIdAndSubjectIdAndSemester(branchId, subjectId, semester)
                        .orElseThrow(() -> new RuntimeException("BranchSubject not found")));
    }

    @PostMapping("")
    @Transactional
    public ResponseEntity<CourseAssignment> createCourseAssignment(@RequestBody ClassAssignmentDto classAssignmentDto) {

        Optional<CourseAssignment> newCourseAssignment = courseAssignmentService.createCourseAssignment(classAssignmentDto);
        if (newCourseAssignment.isEmpty()){
            throw new RuntimeException("Failed to create course assignment");
        }
        return ResponseEntity.ok(newCourseAssignment.get());
    }

    @GetMapping("/assignments")
    public ResponseEntity<List<ViewAssignmentDto>> getCourseAssignments(@RequestParam("branchId") int branchId,
            @RequestParam("semester") int semester,
            @RequestParam("section") String section) {
        return ResponseEntity.ok(
                courseAssignmentService.viewAssignments(branchId, semester, section));
    }
}
