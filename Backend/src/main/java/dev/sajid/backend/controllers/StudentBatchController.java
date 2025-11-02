package dev.sajid.backend.controllers;

import java.util.List;

import dev.sajid.backend.exceptions.ResourceNotFoundException;
import dev.sajid.backend.repositories.BranchRepository;
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

    private final AttendanceService attendanceService;

    private final BranchRepository branchRepository;

    public StudentBatchController(StudentBatchRepository studentBatchRepository, AttendanceService attendanceService, BranchRepository branchRepository) {
        this.studentBatchRepository = studentBatchRepository;
        this.attendanceService = attendanceService;
        this.branchRepository = branchRepository;
    }

    @GetMapping("/semesters")
    public ResponseEntity<List<Integer>> getSemestersByBranchId(
            @RequestParam("branchId") int branchId
    ) {
        checkBranchExistence(branchId);
        List<Integer> semesters = studentBatchRepository.findSemestersByBranchId(branchId);
        return ResponseEntity.ok(semesters);
    }

    @GetMapping("/sections")
    public ResponseEntity<List<String>> getSectionsBySemesterAndBranchId(
            @RequestParam("branchId") int branchId,
            @RequestParam("semester") int semester
    ) {
        checkBranchExistence(branchId);
        List<String> sections = studentBatchRepository.findSectionsByBranchIdAndSemester(branchId, semester);
        return ResponseEntity.ok(sections);
    }

    @GetMapping("/{studentBatchId}/attendance")
    public ResponseEntity<?> getFullAttendance(@PathVariable("studentBatchId") int studentBatchId) {
        checkStudentBatchExistence(studentBatchId);
        return ResponseEntity.ok(attendanceService.calculateFullAttendanceForStudentBatch(studentBatchId));
    }

    private void checkBranchExistence(int branchId){
        if (!branchRepository.existsById(branchId))
            throw new ResourceNotFoundException("Branch not found with ID: " + branchId);
    }
    private void checkStudentBatchExistence(int studentBatchId){
        if (!studentBatchRepository.existsById(studentBatchId))
            throw new ResourceNotFoundException("Student Batch not found with ID: " + studentBatchId);
    }
}
