package dev.sajid.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sajid.backend.repositories.StudentBatchRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/student-batches")
@CrossOrigin
public class StudentBatchController {
    private final StudentBatchRepository studentBatchRepository;

    StudentBatchController(StudentBatchRepository studentBatchRepository) {
        this.studentBatchRepository = studentBatchRepository;
    }

    @GetMapping("/semesters")
    public ResponseEntity<List<Integer>> getSemestersByBranchId(@RequestParam("branchId") int branchId) {
        List<Integer> semesters = studentBatchRepository.findSemestersByBranchId(branchId);
        return ResponseEntity.ok(semesters);
    }

    @GetMapping("/sections")
    public ResponseEntity<List<String>> getSectionsBySemesterAndBranchId(@RequestParam("branchId") int branchId, @RequestParam("semester") int semester) {
        List<String> sections = studentBatchRepository.findSectionsByBranchIdAndSemester(branchId, semester);
        return ResponseEntity.ok(sections);
    }
}
