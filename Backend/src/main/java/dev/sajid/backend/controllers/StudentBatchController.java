package dev.sajid.backend.controllers;

import java.util.List;

import dev.sajid.backend.exceptions.ResourceNotFoundException;
import dev.sajid.backend.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.sajid.backend.repositories.StudentBatchRepository;


@RestController
@RequestMapping("/api/student-batches")
@CrossOrigin
public class StudentBatchController {
    private final StudentBatchRepository studentBatchRepository;

    @Autowired
    private AttendanceService attendanceService;

    StudentBatchController(StudentBatchRepository studentBatchRepository) {
        this.studentBatchRepository = studentBatchRepository;
    }

    @GetMapping("/semesters")
    public ResponseEntity<List<Integer>> getSemestersByBranchId(
            @RequestParam("branchId") int branchId
    ) {
        List<Integer> semesters = studentBatchRepository.findSemestersByBranchId(branchId);
        return ResponseEntity.ok(semesters);
    }

    @GetMapping("/sections")
    public ResponseEntity<List<String>> getSectionsBySemesterAndBranchId(
            @RequestParam("branchId") int branchId,
            @RequestParam("semester") int semester
    ) {
        List<String> sections = studentBatchRepository.findSectionsByBranchIdAndSemester(branchId, semester);
        return ResponseEntity.ok(sections);
    }

    @GetMapping("/{studentBatchId}/attendance")
    public ResponseEntity<?> getFullAttendance(@PathVariable("studentBatchId") int studentBatchId) {
        if (!studentBatchRepository.existsById(studentBatchId)){
            throw new ResourceNotFoundException("A student batch not found with ID: " + studentBatchId);
        }
        return ResponseEntity.ok(attendanceService.calculateFullAttendanceForStudentBatch(studentBatchId));
    }
}
