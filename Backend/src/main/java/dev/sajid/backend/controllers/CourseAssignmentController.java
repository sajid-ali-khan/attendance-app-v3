package dev.sajid.backend.controllers;

import java.util.Map;
import java.util.Optional;

import dev.sajid.backend.exceptions.ResourceNotFoundException;
import dev.sajid.backend.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sajid.backend.dtos.ClassAssignmentDto;
import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import dev.sajid.backend.services.CourseAssignmentService;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/course-assignments")
public class CourseAssignmentController {
    private final StudentBatchRepository studentBatchRepository;
    private final BranchSubjectRepository branchSubjectRepository;
    private final CourseAssignmentService courseAssignmentService;
    private final BranchRepository branchRepository;

    CourseAssignmentController(
            BranchRepository branchRepository,
            StudentBatchRepository studentBatchRepository,
            BranchSubjectRepository branchSubjectRepository,
            CourseAssignmentService courseAssignmentService
    ) {
        this.studentBatchRepository = studentBatchRepository;
        this.branchSubjectRepository = branchSubjectRepository;
        this.courseAssignmentService = courseAssignmentService;
        this.branchRepository = branchRepository;
    }

    @GetMapping("/student_batches")
    public ResponseEntity<?> getStudentBatch(@RequestParam("branchId") int branchId,
                                                        @RequestParam("semester") int semester,
                                                        @RequestParam("section") String section) {
        checkBranchExistence(branchId);
        return ResponseEntity.ok(
                studentBatchRepository.findByBranch_IdAndSemesterAndSection(branchId, semester, section)
                        .orElseThrow(() -> new ResourceNotFoundException("BranchSubject not found.")));
    }

    @GetMapping("/branch-subjects")
    public ResponseEntity<?> getBranchSubjects(@RequestParam("branchId") int branchId,
                                                           @RequestParam("semester") int semester,
                                                           @RequestParam("subjectId") int subjectId) {
        checkBranchExistence(branchId);
        return ResponseEntity
                .ok(branchSubjectRepository.findFirstByBranchIdAndSubjectIdAndSemester(branchId, subjectId, semester)
                        .orElseThrow(() -> new RuntimeException("BranchSubject not found")));
    }

    @PostMapping("")
    @Transactional
    public ResponseEntity<?> createCourseAssignment(@RequestBody ClassAssignmentDto classAssignmentDto) {
        if (classAssignmentDto == null)
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Class Assignment can't be null."
            ));
        Optional<CourseAssignment> newCourseAssignment = courseAssignmentService.createCourseAssignment(classAssignmentDto);
        if (newCourseAssignment.isEmpty()) throw new RuntimeException("Failed to create course assignment");
        return ResponseEntity.ok(newCourseAssignment.get());
    }

    @GetMapping("/assignments")
    public ResponseEntity<?> getCourseAssignments(@RequestParam("branchId") int branchId,
                                                                        @RequestParam("semester") int semester,
                                                                        @RequestParam("section") String section) {
        checkBranchExistence(branchId);
        return ResponseEntity.ok(
                courseAssignmentService.viewAssignments(branchId, semester, section));
    }

    private void checkBranchExistence(int branchId){
        if (!branchRepository.existsById(branchId))
            throw new ResourceNotFoundException("Branch not found with ID: " + branchId);
    }
}
